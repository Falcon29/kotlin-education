class SqlSelectBuilder {
    private var table: String? = null
    private var columns = mutableListOf<String>()
    private var where: SqlExpression? = null

    fun select(vararg columns: String) = this.columns.addAll(columns)

    fun from(table: String) {
        this.table = table
    }

    fun where(expression: SqlExpression) {
        this.where = expression
    }

    fun build(): String {
        require(table != null) { "'table' should not be null" }

        val col = if (columns.isEmpty()) "*" else columns.joinToString(", ")
        val where = where?.let { " where ${it.build()}" } ?: ""

        return "select $col from $table$where"
    }
}

