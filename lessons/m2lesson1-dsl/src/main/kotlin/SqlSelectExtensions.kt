fun query(block: SqlSelectBuilder.() -> Unit): SqlSelectBuilder =
    SqlSelectBuilder().apply(block)

infix fun String.eq(value: String) = SqlEq(SqlCol(this), SqlStrConst(value))

infix fun String.eq(value: Number) = SqlEq(SqlCol(this), SqlNumConst(value))

/*
functions eq() and nonEq() will require either overloading for diff types, or generic impl
e.g. SqlStrConst, SqlNumConst etc
 */
infix fun String.nonEq(value: Number) = SqlNonEq(SqlCol(this), SqlNumConst(value))

infix fun String.nonEq(value: Nothing?) = SqlNotNull(SqlCol(this))

fun or(vararg expr: SqlExpression) = SqlOr(expr.toList())