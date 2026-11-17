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
private val Accent = Color(0xFF8B7CFF)
private val Muted = Color(0xFF9AA8C2)
private val Green = Color(0xFF43D6A0)

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
    val referencePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        referenceUri = it
    }
    val productPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        productUri = it
    }

    MaterialTheme(colorScheme = darkColorScheme(
        background = Bg, surface = Panel, primary = Accent, secondary = Green
    )) {
        Scaffold(
            containerColor = Bg,
            bottomBar = {
                NavigationBar(containerColor = Panel) {
                    listOf(
                        Triple("Home", Icons.Default.Home, "Home"),
                        Triple("Studio", Icons.Default.Image, "Studio"),
                        Triple("Library", Icons.Default.Folder, "Library"),
                        Triple("Settings", Icons.Default.Settings, "Settings")
                    ).forEach { item ->
                        NavigationBarItem(
                            selected = selectedTab == item.first,
                            onClick = { selectedTab = item.first },
                            icon = { Icon(item.second, contentDescription = item.first) },
                            label = { Text(item.third) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Accent, selectedTextColor = Color.White,
                                indicatorColor = Panel2, unselectedIconColor = Muted,
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
                else -> HomeScreen(
                    modifier = Modifier.padding(padding),
                    onOpenStudio = { selectedTab = "Studio" }
                )
            }
        }
    }
}

@Composable
private fun HomeScreen(modifier: Modifier = Modifier, onOpenStudio: () -> Unit) {
    Column(
        modifier.fillMaxSize().background(Bg).verticalScroll(rememberScrollState())
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text("AURA AI", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                Text("AFFILIATE PRODUCTION AGENT", color = Muted, fontSize = 10.sp, letterSpacing = 1.5.sp)
            }
            Surface(shape = CircleShape, color = Color(0xFF173A35)) {
                Row(Modifier.padding(horizontal = 11.dp, vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(7.dp).background(Green, CircleShape))
                    Spacer(Modifier.width(7.dp))
                    Text("PROTOTYPE", color = Green, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        Card(colors = CardDefaults.cardColors(containerColor = Panel2), shape = RoundedCornerShape(22.dp)) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("YOUR AI WORKSPACE", color = Muted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text("Reference Image Studio is ready to test", color = Color.White, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                Text("Primary reference image has been bundled into this prototype. You can also choose another image in Studio.", color = Muted, fontSize = 12.sp)
                Button(onClick = onOpenStudio, modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(14.dp)) {
                    Icon(Icons.Default.Image, null)
                    Spacer(Modifier.width(8.dp))
                    Text("OPEN REFERENCE IMAGE STUDIO")
                }
            }
        }
        SectionTitle("WORKFLOW MODULES", "6 MODULES")
        ModuleCard(Icons.Default.Search, "Product Research", "Find and compare 5 women's flat shoes", "NOT CONNECTED")
        ModuleCard(Icons.Default.ShoppingBag, "Product Collector", "Product images and color variants", "NOT CONNECTED")
        ModuleCard(Icons.Default.Image, "Reference Image Studio", "Primary reference + product image + prompt", "LOCAL PREVIEW")
        ModuleCard(Icons.Default.Movie, "Flow Controller", "Google Flow • 9:16 • 720p • 8 seconds", "NOT CONNECTED")
        ModuleCard(Icons.Default.VideoSettings, "Video Processor", "Save and process approved video at 60 FPS", "PLANNED")
        ModuleCard(Icons.Default.Send, "Publishing & Report", "TikTok approval and WhatsApp report", "PLANNED")
        Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(18.dp)) {
            Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text("IMPORTANT", color = Color(0xFFFFD28A), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                Text("This is still a prototype. No AI image generation, TikTok access, Google Flow automation, or publishing is connected yet.", color = Muted, fontSize = 12.sp)
            }
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
        modifier.fillMaxSize().background(Bg).verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("REFERENCE IMAGE STUDIO", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("Atur gambar acuan dan foto produk sebelum proses generasi gambar diaktifkan.", color = Muted, fontSize = 12.sp)
        Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(18.dp)) {
            Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("PRIMARY REFERENCE IMAGE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                if (referenceUri != null) {
                    AsyncImage(model = referenceUri, contentDescription = "Selected reference image",
                        modifier = Modifier.fillMaxWidth().height(300.dp))
                } else {
                    Image(painter = painterResource(R.drawable.aura_primary_reference),
                        contentDescription = "AURA primary shoe reference",
                        modifier = Modifier.fillMaxWidth().height(300.dp))
                }
                Text("Acuan bawaan: POV dari atas, sepatu dipegang dan dipakai, lantai kayu, cermin, outfit putih, cahaya indoor hangat.", color = Muted, fontSize = 11.sp)
                OutlinedButton(onClick = onPickReference, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Upload, null)
                    Spacer(Modifier.width(8.dp))
                    Text("GANTI GAMBAR REFERENSI")
                }
            }
        }
        Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(18.dp)) {
            Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("PRODUCT IMAGE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                if (productUri != null) {
                    AsyncImage(model = productUri, contentDescription = "Selected product image",
                        modifier = Modifier.fillMaxWidth().height(220.dp))
                } else {
                    Box(Modifier.fillMaxWidth().height(120.dp).background(Panel2, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center) {
                        Text("Belum ada foto produk dipilih", color = Muted, fontSize = 12.sp)
                    }
                }
                OutlinedButton(onClick = onPickProduct, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.AddPhotoAlternate, null)
                    Spacer(Modifier.width(8.dp))
                    Text("PILIH FOTO PRODUK")
                }
            }
        }
        Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(18.dp)) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("PROMPT TEMPLATE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                OutlinedTextField(value = prompt, onValueChange = onPromptChange,
                    modifier = Modifier.fillMaxWidth(), minLines = 4,
                    placeholder = { Text("Prompt khusus akan ditambahkan nanti...") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                        focusedBorderColor = Accent, unfocusedBorderColor = Muted,
                        focusedPlaceholderColor = Muted, unfocusedPlaceholderColor = Muted
                    ))
                Text("Status: generator belum terhubung. Tombol generate belum diaktifkan agar tidak memberi kesan gambar sudah diproses.", color = Color(0xFFFFD28A), fontSize = 11.sp)
                Button(onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.AutoAwesome, null)
                    Spacer(Modifier.width(8.dp))
                    Text("GENERATE IMAGE — COMING LATER")
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String, tag: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        Text(tag, color = Muted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ModuleCard(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String, status: String) {
    Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(16.dp)) {
        Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = RoundedCornerShape(12.dp), color = Panel2) {
                Box(Modifier.size(44.dp), contentAlignment = Alignment.Center) { Icon(icon, null, tint = Accent) }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(title, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text(subtitle, color = Muted, fontSize = 10.sp)
            }
            Text(status, color = Color(0xFFFFC66D), fontSize = 8.sp, fontWeight = FontWeight.Bold)
        }
    }
}
