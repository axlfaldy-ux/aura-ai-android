package com.aura.agent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

private val Bg = Color(0xFF0B1020)
private val Panel = Color(0xFF151E32)
private val Panel2 = Color(0xFF1C2942)
private val Accent = Color(0xFF8B9EFF)
private val Muted = Color(0xFF9AA8C2)
private val Green = Color(0xFF43D6A0)
private val Gold = Color(0xFFFFC66D)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AuraApp() }
    }
}

@Composable
fun AuraApp() {
    var selectedTab by remember { mutableStateOf("Home") }
    var referenceUri by remember { mutableStateOf<android.net.Uri?>(null) }
    var productUri by remember { mutableStateOf<android.net.Uri?>(null) }
    var prompt by remember { mutableStateOf("") }

    val referencePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { referenceUri = it }

    val productPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { productUri = it }

    MaterialTheme(
        colorScheme = darkColorScheme(
            background = Bg,
            surface = Panel,
            primary = Accent,
            secondary = Green
        )
    ) {
        Scaffold(
            containerColor = Bg,
            bottomBar = {
                NavigationBar(
                    containerColor = Panel,
                    contentColor = Color.White
                ) {
                    val tabs = listOf(
                        Triple("Home", Icons.Default.Home, "Home"),
                        Triple("Studio", Icons.Default.Image, "Studio"),
                        Triple("Library", Icons.Default.Folder, "Library"),
                        Triple("Settings", Icons.Default.Settings, "Settings")
                    )
                    tabs.forEach { item ->
                        NavigationBarItem(
                            selected = selectedTab == item.first,
                            onClick = { selectedTab = item.first },
                            icon = { Icon(item.second, contentDescription = item.third) },
                            label = { Text(item.third) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Accent,
                                selectedTextColor = Color.White,
                                indicatorColor = Panel2,
                                unselectedIconColor = Muted,
                                unselectedTextColor = Muted
                            )
                        )
                    }
                }
            }
        ) { padding ->
            when (selectedTab) {
                "Studio" -> ReferenceStudio(
                    modifier = Modifier.padding(padding),
                    referenceUri = referenceUri,
                    productUri = productUri,
                    prompt = prompt,
                    onPromptChange = { prompt = it },
                    onPickReference = { referencePicker.launch("image/*") },
                    onPickProduct = { productPicker.launch("image/*") }
                )
                "Library" -> LibraryScreen(Modifier.padding(padding))
                "Settings" -> SettingsScreen(Modifier.padding(padding))
                else -> HomeScreen(
                    modifier = Modifier.padding(padding),
                    onOpenStudio = { selectedTab = "Studio" }
                )
            }
        }
    }
}

@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    onOpenStudio: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Bg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(13.dp),
                    color = Panel2,
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.AutoAwesome, null, tint = Accent, modifier = Modifier.size(24.dp))
                    }
                }
                Spacer(Modifier.width(11.dp))
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("AURA AI", color = Color.White, fontSize = 21.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                    Text("AFFILIATE WORKSPACE", color = Muted, fontSize = 9.sp, letterSpacing = 1.5.sp)
                }
            }
            Surface(shape = CircleShape, color = Color(0xFF173A35)) {
                Row(
                    Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.size(6.dp).background(Green, CircleShape))
                    Spacer(Modifier.width(6.dp))
                    Text("READY", color = Green, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = Panel2),
            shape = RoundedCornerShape(22.dp)
        ) {
            Column(
                Modifier.fillMaxWidth().padding(19.dp),
                verticalArrangement = Arrangement.spacedBy(11.dp)
            ) {
                Text("WELCOME TO YOUR WORKSPACE", color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp)
                Text(
                    "Create. Generate.\nEarn.",
                    color = Color.White,
                    fontSize = 30.sp,
                    lineHeight = 35.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    "Kelola produksi konten affiliate dari pemilihan produk hingga video siap disimpan.",
                    color = Muted,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
                Button(
                    onClick = onOpenStudio,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Accent)
                ) {
                    Icon(Icons.Default.Add, null)
                    Spacer(Modifier.width(8.dp))
                    Text("OPEN AI STUDIO", fontWeight = FontWeight.Bold)
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.ShoppingBag,
                number = "0",
                label = "Active Products"
            )
            StatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Movie,
                number = "0",
                label = "Videos Created"
            )
        }

        SectionTitle("PRODUCTION WORKFLOW", "5 STEPS")
        WorkflowCard("01", Icons.Default.Search, "Product Research", "Cari dan bandingkan produk affiliate")
        WorkflowCard("02", Icons.Default.ShoppingBag, "Product Selection", "Pilih satu produk untuk diproses")
        WorkflowCard("03", Icons.Default.Image, "AI Image Studio", "Siapkan gambar dan prompt visual")
        WorkflowCard("04", Icons.Default.Movie, "Flow Video Studio", "Rencanakan pembuatan video")
        WorkflowCard("05", Icons.Default.Folder, "Project Library", "Simpan dan kelola hasil produksi")

        Card(
            colors = CardDefaults.cardColors(containerColor = Panel),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth().padding(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(Icons.Default.Info, null, tint = Gold, modifier = Modifier.size(19.dp))
                Spacer(Modifier.width(10.dp))
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("PROTOTYPE STATUS", color = Gold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(
                        "Tampilan dashboard sudah disiapkan. Generator gambar, Google Flow, dan otomatisasi publikasi belum terhubung.",
                        color = Muted,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }
        Spacer(Modifier.height(4.dp))
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    number: String,
    label: String
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Panel),
        shape = RoundedCornerShape(17.dp)
    ) {
        Column(
            Modifier.fillMaxWidth().padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Icon(icon, null, tint = Accent, modifier = Modifier.size(21.dp))
            Text(number, color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.Bold)
            Text(label, color = Muted, fontSize = 10.sp)
        }
    }
}

@Composable
private fun WorkflowCard(
    number: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Panel),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(number, color = Accent, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.width(12.dp))
            Surface(shape = RoundedCornerShape(11.dp), color = Panel2) {
                Box(Modifier.size(40.dp), contentAlignment = Alignment.Center) {
                    Icon(icon, null, tint = Accent, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(Modifier.width(11.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text(subtitle, color = Muted, fontSize = 10.sp)
            }
            Icon(Icons.Default.ChevronRight, null, tint = Muted)
        }
    }
}

@Composable
private fun LibraryScreen(modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxSize().background(Bg).verticalScroll(rememberScrollState()).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("PROJECT LIBRARY", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Semua proyek dan hasil produksi affiliate akan tersimpan di sini.", color = Muted, fontSize = 12.sp)
        Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(18.dp)) {
            Column(
                Modifier.fillMaxWidth().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                Icon(Icons.Default.FolderOpen, null, tint = Accent, modifier = Modifier.size(42.dp))
                Text("Belum ada proyek", color = Color.White, fontWeight = FontWeight.Bold)
                Text("Proyek yang selesai dibuat akan muncul di halaman ini.", color = Muted, fontSize = 11.sp)
            }
        }
    }
}

@Composable
private fun SettingsScreen(modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxSize().background(Bg).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("SETTINGS", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Pengaturan AURA AI", color = Muted, fontSize = 12.sp)
        Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(16.dp)) {
            Column(Modifier.fillMaxWidth().padding(15.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SettingRow(Icons.Default.Palette, "Appearance", "Premium Dark")
                HorizontalDivider(color = Panel2)
                SettingRow(Icons.Default.Language, "Language", "Bahasa Indonesia")
                HorizontalDivider(color = Panel2)
                SettingRow(Icons.Default.Info, "App version", "AURA AI Prototype")
            }
        }
        Text(
            "Pengaturan ini masih berupa tampilan awal. Preferensi belum disimpan secara permanen.",
            color = Muted,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun SettingRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String
) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = Accent, modifier = Modifier.size(21.dp))
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Text(value, color = Muted, fontSize = 10.sp)
        }
    }
}

@Composable
private fun ReferenceStudio(
    modifier: Modifier = Modifier,
    referenceUri: android.net.Uri?,
    productUri: android.net.Uri?,
    prompt: String,
    onPromptChange: (String) -> Unit,
    onPickReference: () -> Unit,
    onPickProduct: () -> Unit
) {
    Column(
        modifier.fillMaxSize().background(Bg).verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("AI IMAGE STUDIO", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Siapkan gambar acuan, foto produk, dan prompt untuk proses kreatif.", color = Muted, fontSize = 12.sp)

        Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(18.dp)) {
            Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("PRIMARY REFERENCE IMAGE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                if (referenceUri != null) {
                    AsyncImage(
                        model = referenceUri,
                        contentDescription = "Selected reference image",
                        modifier = Modifier.fillMaxWidth().height(280.dp)
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.aura_primary_reference),
                        contentDescription = "AURA primary reference",
                        modifier = Modifier.fillMaxWidth().height(280.dp)
                    )
                }
                Text("Gambar acuan utama untuk menjaga konsistensi visual.", color = Muted, fontSize = 11.sp)
                OutlinedButton(onClick = onPickReference, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Upload, null)
                    Spacer(Modifier.width(8.dp))
                    Text("CHANGE REFERENCE IMAGE")
                }
            }
        }

        Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(18.dp)) {
            Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("PRODUCT IMAGE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                if (productUri != null) {
                    AsyncImage(
                        model = productUri,
                        contentDescription = "Selected product image",
                        modifier = Modifier.fillMaxWidth().height(210.dp)
                    )
                } else {
                    Box(
                        Modifier.fillMaxWidth().height(115.dp).background(Panel2, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Belum ada foto produk dipilih", color = Muted, fontSize = 12.sp)
                    }
                }
                OutlinedButton(onClick = onPickProduct, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.AddPhotoAlternate, null)
                    Spacer(Modifier.width(8.dp))
                    Text("SELECT PRODUCT IMAGE")
                }
            }
        }

        Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(18.dp)) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("PROMPT TEMPLATE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                OutlinedTextField(
                    value = prompt,
                    onValueChange = onPromptChange,
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4,
                    placeholder = { Text("Tulis prompt kreatif di sini...") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Accent,
                        unfocusedBorderColor = Muted,
                        focusedPlaceholderColor = Muted,
                        unfocusedPlaceholderColor = Muted
                    )
                )
                Text(
                    "Generator gambar belum terhubung. Prompt dan gambar hanya disiapkan sebagai preview.",
                    color = Gold,
                    fontSize = 11.sp
                )
                Button(onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.AutoAwesome, null)
                    Spacer(Modifier.width(8.dp))
                    Text("IMAGE GENERATION COMING SOON")
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String, tag: String) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        Text(tag, color = Muted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
    }
}