package com.example.myfinance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfinance.ui.theme.MyfinanceTheme
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyfinanceTheme {
                FinanceApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceApp() {
    var transactions by remember { mutableStateOf(listOf<Transaction>()) }
    var amount by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(TransactionType.INCOME) }
    var category by remember { mutableStateOf("") }

    val incomeCategories = listOf("Gaji", "Bonus", "Investasi", "Lainnya")
    val expenseCategories = listOf("Makanan", "Transportasi", "Sewa", "Belanja", "Lainnya")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("Catatan Keuangan") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Input Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedType == TransactionType.INCOME,
                            onClick = { selectedType = TransactionType.INCOME; category = "" }
                        )
                        Text("Pemasukan")
                        Spacer(modifier = Modifier.width(8.dp))
                        RadioButton(
                            selected = selectedType == TransactionType.EXPENSE,
                            onClick = { selectedType = TransactionType.EXPENSE; category = "" }
                        )
                        Text("Pengeluaran")
                    }

                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text("Nominal") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Pilih Kategori:", fontWeight = FontWeight.Bold)
                    val currentCategories = if (selectedType == TransactionType.INCOME) incomeCategories else expenseCategories
                    
                    Row(modifier = Modifier.padding(vertical = 4.dp)) {
                        currentCategories.forEach { cat ->
                            FilterChip(
                                selected = category == cat,
                                onClick = { category = cat },
                                label = { Text(cat) },
                                modifier = Modifier.padding(end = 4.dp)
                            )
                        }
                    }

                    Button(
                        onClick = {
                            if (amount.isNotEmpty() && category.isNotEmpty()) {
                                val newTransaction = Transaction(
                                    id = transactions.size + 1,
                                    type = selectedType,
                                    category = category,
                                    amount = amount.toDoubleOrNull() ?: 0.0,
                                    date = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
                                )
                                transactions = listOf(newTransaction) + transactions
                                amount = ""
                                category = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Tambah Transaksi")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Riwayat Transaksi", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(transactions) { item ->
                    TransactionItem(item)
                }
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (transaction.type == TransactionType.INCOME) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = transaction.category, fontWeight = FontWeight.Bold)
                Text(text = transaction.date, fontSize = 12.sp)
            }
            Text(
                text = if (transaction.type == TransactionType.INCOME) "+Rp ${transaction.amount}" else "-Rp ${transaction.amount}",
                color = if (transaction.type == TransactionType.INCOME) Color(0xFF2E7D32) else Color(0xFFC62828),
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}
