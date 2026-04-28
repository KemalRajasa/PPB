package com.example.myfinance

enum class TransactionType {
    INCOME, EXPENSE
}

data class Transaction(
    val id: Int,
    val type: TransactionType,
    val category: String,
    val amount: Double,
    val date: String
)
