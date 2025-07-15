data class SqlEq(
    val left: SqlExpression,
    val right: SqlExpression
) : SqlExpression {

    override fun build(): String = "${left.build()} = ${right.build()}"
}

class SqlNonEq(
    val left: SqlExpression,
    val right: SqlExpression
) : SqlExpression {

    override fun build(): String = "${left.build()} != ${right.build()}"
}

class SqlNotNull(
    val left: SqlExpression
) : SqlExpression {

    override fun build(): String = "${left.build()} not NULL"
}

data class SqlCol(
    val name: String
) : SqlExpression {

    override fun build(): String = name
}

data class SqlStrConst(
    val value: String
) : SqlExpression {

    override fun build(): String = "'$value'"
}

data class SqlNumConst(
    val value: Number
) : SqlExpression {

    override fun build(): String = "$value"
}

data class SqlOr(
    val args: List<SqlExpression>
) : SqlExpression {

    override fun build(): String = args.joinToString(" or ", "(", ")") {
        it.build()
    }
}