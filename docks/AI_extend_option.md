Nice — great practical question. I’ll give you a direct, developer-friendly guide for *adding a new entity `Faq`* to your project. It will explain **what** to add/change, **where** to put it (file/module paths you already have), **why** to do it that way, and include concrete code sketches you can copy/paste and adapt.

I assume the existing project structure you described (modules for `-common`, `-biz`, `-api-v2`, `-repo-*`, `-app-ktor`, `-app-common`, COR DSL, etc.). I’ll show both a minimal (fast) approach and a cleaner, scalable approach.

---

# Overview: new entity `Faq` — high-level work items

1. Add domain model for `Faq` (common).
2. Add transport models (API v2) and mappers (fromTransport / toTransport).
3. Extend repository interface(s) with FAQ CRUD (new `IRepoFaq` or extend `IRepo*`).
4. Implement repository(s): in-memory, stubs, and/or Cassandra.
5. Add migrations / CQL table for Cassandra.
6. Wire repo instances into `corSettings` / `InitRepo` so processor can pick correct repo by `workMode`.
7. Extend `CCContext` (or add dedicated `FaqContext` fields) to carry request/response data for FAQ flows.
8. Update `CCProcessor`: add new COR operation(s) (GET_FAQ, UPDATE_FAQ) or create a new processor for FAQs.
9. Add controller/routes in `app-ktor` (v2 endpoints `/v2/faq/get`, `/v2/faq/update`) and map to controller helper.
10. Add mapping tests and unit/integration tests.
11. Update docs/specs (`specs/`), Gradle settings, and Docker migration files.
12. Optionally: WS notifications (use `wsSessionRepo.sendAll()` to broadcast FAQ updates to connected clients).

---

# Detailed step-by-step (what / where / why) — copy/paste ready

> Use these file paths as examples — adapt to your module names.

---

## 1) Domain model — add `Faq` entity (shared/common)

**Where:** `contact-center-common/src/commonMain/kotlin/models/Faq.kt`

**What / Why:** shared data model used everywhere; analogous to `CCTicket`.

**Example:**

```kotlin
// contact-center-common/src/commonMain/kotlin/models/Faq.kt
package com.example.contactcenter.common.models

import kotlinx.serialization.Serializable
import java.util.UUID // or your multiplatform id approach

@Serializable
data class Faq(
    val id: String = UUID.randomUUID().toString(),
    val topic: String = "",
    val answer: String = "",
    val updatedAt: String? = null // or Instant, depending on your existing conventions
)
```

---

## 2) Transport models & mappers (API v2)

**Where:** `contact-center-api-v2-multiplatform` (or whichever module holds v2 DTOs)

**What:** transport request/response DTOs and mapping functions to/from `CCContext`.

**Files to add:**

* `FaqRequest.kt` (get/update request DTOs)
* `FaqResponse.kt`
* `FaqMappers.kt` (fromTransport / toTransport functions)

**Examples:**

```kotlin
// contact-center-api-v2-multiplatform/src/commonMain/kotlin/api/v2/faq/FaqRequest.kt
@Serializable
data class ApiFaqGetRequest(val id: String? = null, val topic: String? = null)

@Serializable
data class ApiFaqUpdateRequest(val id: String, val topic: String? = null, val answer: String? = null)

@Serializable
data class ApiFaqResponse(val faq: ApiFaq? = null, val errors: List<ApiError> = emptyList())

@Serializable
data class ApiFaq(val id: String, val topic: String, val answer: String, val updatedAt: String? = null)
```

Mapper functions (example):

```kotlin
// contact-center-api-v2-multiplatform/.../FaqMappers.kt
fun CCContext.fromTransportFaqGet(req: ApiFaqGetRequest) {
    this.command = CCCommand.GET_FAQ
    this.requestFaqId = req.id ?: ""
    this.requestFaqTopic = req.topic ?: ""
}

fun CCContext.toTransportFaq(): ApiFaqResponse {
    val faq = this.responseFaq?.let { ApiFaq(it.id, it.topic, it.answer, it.updatedAt) }
    return ApiFaqResponse(faq = faq, errors = this.errors.map { it.toApiError() })
}
```

**Why:** transport models define API surface; mappers convert network objects ↔ internal context.

---

## 3) Repository interface for FAQ

Two choices:

* A. **Separate interface** `IRepoFaq` — clean separation, easier to test.
* B. **Add methods to existing `IRepoTicket`** — less files but less clear.

**Recommended:** create `IRepoFaq`.

**Where:** `contact-center-common/src/commonMain/kotlin/repository/IRepoFaq.kt`

**Example:**

```kotlin
interface IRepoFaq {
    suspend fun createFaq(request: DtoCreateFaqRequest): DtoFaqResponse
    suspend fun getFaq(request: DtoGetFaqRequest): DtoFaqResponse
    suspend fun updateFaq(request: DtoUpdateFaqRequest): DtoFaqResponse
}
```

Add DTOs for DB layer (DtoGetFaqRequest, DtoFaqResponse, etc.) consistent with how ticket repo uses `IDB*` types.

**Why:** keeps domain logic decoupled from persistence details and mirrors ticket repo design.

---

## 4) Repo implementations

**Where:** new modules or reuse existing naming pattern:

* `contact-center-repo-inmemory` → add `InMemoryFaqRepo`
* `contact-center-repo-stubs` → stubbed Faq responses
* `contact-center-repo-cassandra` → Cassandra table + DAO + mapping

**InMemory example:**

```kotlin
class RepoFaqInMemory : IRepoFaq {
    private val storage = ConcurrentHashMap<String, Faq>()

    override suspend fun createFaq(request: DtoCreateFaqRequest) = ...
    override suspend fun getFaq(request: DtoGetFaqRequest): DtoFaqResponse = ...
    override suspend fun updateFaq(request: DtoUpdateFaqRequest): DtoFaqResponse = ...
}
```

**Cassandra changes:**

* Add `FaqCassandraDTO.kt` mapping to `faq` table columns.
* Add prepared statements for `SELECT`, `INSERT`, `UPDATE`.
* Add repository implementation using your existing Cassandra session factory.

**Why:** follow project pattern — each repo type mirrors ticket repo.

---

## 5) DB migrations for Cassandra

**Where:** `contact-center-repo-cassandra/src/main/resources/migrations/` or `deploy/` scripts.

**Add CQL:**

```sql
CREATE TABLE IF NOT EXISTS contact_center.faq (
  id text PRIMARY KEY,
  topic text,
  answer text,
  updated_at timestamp
);
```

Add to any test init scripts used by docker-compose or test harness.

**Why:** ensure schema exists for Cassandra repo.

---

## 6) Wire repositories into corSettings / InitRepo

**Where:** `contact-center-biz` (`GetDBConfig.kt` / `InitRepo` worker).

Add new properties in `CorSettings` (or the settings holder):

```kotlin
data class CorSettings(
  val repoFaqProd: IRepoFaq,
  val repoFaqTest: IRepoFaq,
  val repoFaqStub: IRepoFaq,
  // existing repoTicket...
)
```

In `InitRepo` worker (in `CCProcessor` or initialization code) set:

```kotlin
ctx.repoFaq = when(ctx.workMode) {
   CCWorkMode.TEST -> corSettings.repoFaqTest
   CCWorkMode.STUB -> corSettings.repoFaqStub
   else -> corSettings.repoFaqProd
}
```

**Why:** the processor needs to pick the right repo implementation depending on `workMode` (same pattern as ticket).

---

## 7) Extend `CCContext` or create separate context fields

You have to carry FAQ request/response data through COR chain.

**Options:**

* A. **Extend the same `CCContext`** with FAQ-specific fields (`requestFaqId`, `requestFaqTopic`, `responseFaq`, `faqRepoResponse`). This is quick and consistent with small systems.
* B. **Create nested `FaqContext` inside `CCContext`** (more structured).

**Suggested (simple & consistent):**
Add fields in `CCContext` in `contact-center-common`:

```kotlin
// CCContext.kt (add)
var requestFaqId: String = ""
var requestFaqTopic: String = ""
var requestFaqAnswer: String = ""
var responseFaq: Faq? = null
var faqRepoResponse: DtoFaqResponse? = null
```

**Why:** Adding to existing context keeps single execution pipeline and reuses COR executor; COR DSL expects single context type.

---

## 8) Processor changes — where business logic lives

**Where:** `contact-center-biz/src/commonMain/kotlin/CCProcessor.kt`

**Approach choices:**

* A. **Add new `operation` blocks** inside `rootChain` for FAQ commands (recommended).
* B. Create a separate processor (e.g., `FaqProcessor`) — heavier but useful if domain grows large.

**Recommended quick approach:** add new operations.

**Add to `CCCommand` enum:**

```kotlin
enum class CCCommand { CREATE, GET, UPDATE, ASSIGN, GET_FAQ, UPDATE_FAQ /*...*/ }
```

**Add operation to `CCProcessor`:**

```kotlin
operation("FAQ get", CCCommand.GET_FAQ) {
    stubs { /* if workMode==STUB return stubbed faq */ }
    validation {
        worker("Validate FAQ get") { /* ensure id or topic present else add error */ }
    }
    chain {
        worker("Init repo faq") { /* ctx.repoFaq set in InitRepo or here */ }
        worker("Repo: get faq") {
            val req = DtoGetFaqRequest(id = ctx.requestFaqId, topic = ctx.requestFaqTopic)
            val dbRes = ctx.repoFaq.getFaq(req)
            ctx.faqRepoResponse = dbRes
            ctx.responseFaq = dbRes.faq // map DB -> domain model
        }
    }
    prepareResult("Prepare FAQ get") {
        worker("To transport faq result") { /* set ctx.responseFaq etc. */ }
    }
}
```

Similarly for `UPDATE_FAQ`:

* Validation: check `id` and `answer` present.
* Chain: call `repoFaq.updateFaq` and set result.
* PrepareResult: set `ctx.responseFaq`.

**Why:** keep processing unified in the CCProcessor and reuse infrastructure: stubs, errors, logging, repo selection.

---

## 9) Controller & routes

**Where:** `contact-center-app-ktor/src/commonMain/kotlin/v2/FaqRoutes.kt` (new file)

**Add routes:**

```kotlin
fun Route.v2Faq(appSettings: AppSettings) {
    route("/faq") {
        post("/get") {
            processV2(
                onRequest = { req -> ctx.fromTransportFaqGet(req) },
                onResponse = { ctx.toTransportFaq() }
            )
        }
        post("/update") {
            processV2(
                onRequest = { req -> ctx.fromTransportFaqUpdate(req) },
                onResponse = { ctx.toTransportFaq() }
            )
        }
    }
}
```

And register `v2Faq(appSettings)` inside the main `v2` routing in `Application.kt`.

**Why:** follow same `processV2` pattern used for ticket endpoints for consistency.

---

## 10) Mappers update (transport ↔ context)

We already touched on mappers; ensure you implement both:

* `fromTransportFaqGet`, `fromTransportFaqUpdate` (populate ctx).
* `toTransportFaq` (convert `ctx.responseFaq` -> ApiFaqResponse).

Place them alongside other v2 mappers, e.g. in `contact-center-api-v2-multiplatform`.

---

## 11) WebSocket behavior (optional)

If you want WS clients to receive FAQ updates:

* After `UPDATE_FAQ` worker completes successfully, call:

```kotlin
corSettings.wsSession.sendAll(ctx.toTransportFaq().toJsonString())
```

or better: publish a `FaqUpdated` event object and let `wsSessionRepo.sendAll(event)` handle serialization.

**Why:** this uses the existing `sendAll()` which currently is unused; now it’s used to broadcast FAQ changes.

---

## 12) Tests

**Add tests:**

* Unit tests for mappers (transport ↔ domain).
* Biz unit tests: create a test that builds `CCContext`, sets `command = GET_FAQ`, calls `processor.exec(ctx)` and asserts `responseFaq`.
* Repo tests: test `RepoFaqInMemory` and `RepoFaqCassandra` (integration) using test docker-compose for Cassandra.

**Where:**

* `contact-center-biz/src/commonTest/...`
* `contact-center-repo-inmemory/src/test/...`
* `contact-center-repo-cassandra/src/test/...`

**Why:** keep confidence and mirror testing pattern used for tickets.

---

## 13) API Spec and docs

Update `specs/` (OpenAPI) with new endpoints:

* `POST /v2/faq/get`
* `POST /v2/faq/update`

Add DTO schemas for `ApiFaq`, `ApiFaqGetRequest`, `ApiFaqUpdateRequest`.

**Why:** documentation and api contract for clients and tests.

---

## 14) Build & Gradle configuration

**Actions:**

* Add new source sets / modules to `settings.gradle.kts` if you create new modules (`-repo-faq`).
* Update `build-plugin` if it enforces a list of modules or registers tasks per module (likely not necessary unless plugin enumerates modules).
* Ensure new modules are referenced from parent project or other modules (e.g., `contact-center-biz` depends on `contact-center-common` and `contact-center-repo-*`).

**Why:** Gradle needs to know about new modules for compilation and packaging.

---

## 15) Deployment & Docker

If you added Cassandra repo:

* Add CQL migration to `deploy/` or include in Cassandra init scripts.
* If you use Docker images for repo services ensure the DB container is started before the app.

If only in-memory / stub repo:

* No DB changes required for deployment.

---

## 16) Security / Auth considerations

* If FAQ updates should be restricted to operators:

  * Enforce authorization in controller: check `ctx.principal` / `ctx.user.role`.
  * Add JWT role checks in Ktor routes or inside validation worker(s).
* For WS updates: ensure that `sendAll()` only broadcasts authoritatively or send event only to sessions with proper permissions.

**Why:** avoid unauthorized FAQ changes.

---

## 17) Example patches / code snippets (quick)

### Add CCCommand

```kotlin
enum class CCCommand {
    CREATE, GET, UPDATE, ASSIGN,
    GET_FAQ, UPDATE_FAQ
}
```

### CCContext additions

```kotlin
// contact-center-common/src/commonMain/kotlin/CCContext.kt
var requestFaqId: String = ""
var requestFaqTopic: String = ""
var requestFaqAnswer: String = ""
var responseFaq: Faq? = null
var faqRepoResponse: DtoFaqResponse? = null
lateinit var repoFaq: IRepoFaq
```

### Processor skeleton

```kotlin
operation("FAQ get", CCCommand.GET_FAQ) {
    validation {
        worker("faq-validate") {
            if (ctx.requestFaqId.isBlank() && ctx.requestFaqTopic.isBlank()) {
                ctx.addError("faq-id-or-topic-missing")
                ctx.state = CCState.FAILED
            }
        }
    }
    chain {
        worker("init-repo-faq") { /* ctx.repoFaq already set by InitRepo */ }
        worker("repo-get-faq") {
            val dbReq = DtoGetFaqRequest(ctx.requestFaqId, ctx.requestFaqTopic)
            val res = ctx.repoFaq.getFaq(dbReq)
            ctx.faqRepoResponse = res
            ctx.responseFaq = res.faq
            if (res.isNotFound()) ctx.addError("faq-not-found")
        }
    }
    prepareResult("faq-prepare-response") {
        worker("map-faq") { /* map ctx.responseFaq -> ctx.response object */ }
    }
}
```

---

## 18) When to create a separate processor vs extend current

* **Extend `CCProcessor`** if:

  * `Faq` is small/simple.
  * You want to reuse existing `CorSettings`, logging, error handling.
* **Create `FaqProcessor`** if:

  * `Faq` logic will grow big and you prefer separation of concerns.
  * You want separate lifecycle, tests, or different chains.

Either way, keep `CCContext` type consistent or create a parent context that both processors accept.

---

## 19) Checklist (practical order to implement)

1. Create `Faq` model in `-common`.
2. Add transport DTOs and mappers in `api-v2`.
3. Add `IRepoFaq` to `-common` (DB layer DTOs too).
4. Implement `RepoFaqInMemory` and `RepoFaqStub`.
5. Wire repo into `corSettings` and `InitRepo`.
6. Extend `CCContext` with FAQ fields.
7. Add `CCCommand` enum values.
8. Add `operation` blocks in `CCProcessor`.
9. Add Ktor routes and register them.
10. Update OpenAPI spec.
11. Add unit tests for mappers, biz chain, and repo.
12. (If Cassandra): add CQL migration, implement `RepoFaqCassandra`, add tests that spin up Cassandra.
13. Update Gradle files if new modules were created.
14. Optionally add WS broadcast after `UPDATE_FAQ`.
15. Run `./gradlew test` and integration tests.

---

## 20) Common pitfalls & tips

* **Mapping fields:** Keep DB DTO ↔ domain ↔ transport mappers consistent. Name collisions often cause silent bugs.
* **Context bloat:** If `CCContext` gets very large, consider nested contexts or separate processors.
* **Stubbing & tests:** Add stubs early — makes local testing easier (set `workMode = STUB`).
* **Cassandra types:** watch timestamps and timezones; use the same serialization format as other entities.
* **Authorization:** validate roles in controller or as a worker early in the chain.
* **Concurrency in repo:** In-memory repo needs concurrency-safe maps (ConcurrentHashMap). For update semantics mimic DB behavior.
* **sendAll usage:** ensure serialization happens once or in a coroutine; handle slow sessions gracefully (timeouts).

---

## Final quick example: Minimal code flow for GET FAQ

1. Client `POST /v2/faq/get { "id":"..." }`
2. Ktor route calls `processV2()`:

   * `receive<ApiFaqGetRequest>()`
   * `ctx.fromTransportFaqGet(req)`
   * `processor.exec(ctx)` runs `GET_FAQ` operation
   * `controllerHelper` then calls `ctx.toTransportFaq()` and returns JSON

---
