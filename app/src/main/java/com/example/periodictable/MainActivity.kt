package com.example.periodictable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { App() } }
    }
}

@Composable
fun App() {
    var selected by remember { mutableStateOf<Element?>(null) }
    val byPos = remember { ELEMENTS.associateBy { pos(it.z) } }

    Surface(Modifier.fillMaxSize()) {
        Column(Modifier.statusBarsPadding().verticalScroll(rememberScrollState())) {
            Text("الجدول الدوري للعناصر", fontSize = 22.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp))
            // الجدول بترتيبه القياسي (يسار ← يمين)
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Box(Modifier.horizontalScroll(rememberScrollState()).padding(8.dp)) {
                    Column {
                        for (r in 1..10) Row {
                            for (c in 1..18) {
                                val e = byPos[r to c]
                                if (e == null) Spacer(Modifier.size(54.dp)) else Cell(e) { selected = it }
                            }
                        }
                    }
                }
            }
            Column(Modifier.padding(16.dp)) {
                Cat.values().forEach {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(2.dp)) {
                        Box(Modifier.size(16.dp).background(Color(it.color)))
                        Text("  " + it.ar, fontSize = 13.sp)
                    }
                }
            }
        }
    }
    selected?.let { Details(it) { selected = null } }
}

@Composable
fun Cell(e: Element, onClick: (Element) -> Unit) {
    Column(
        Modifier.padding(1.dp).size(52.dp).clip(RoundedCornerShape(4.dp))
            .background(Color(category(e.z).color)).clickable { onClick(e) },
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        Text("${e.z}", fontSize = 9.sp, color = Color.Black)
        Text(e.symbol, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color.Black)
    }
}

@Composable
fun Details(e: Element, onDismiss: () -> Unit) {
    val neutrons = (e.mass.toDouble().roundToInt() - e.z)
    val rows = listOf(
        "العدد الذري" to "${e.z}",
        "الرمز" to e.symbol,
        "الاسم بالإنجليزية" to e.en,
        "الكتلة الذرية" to "${e.mass} u",
        "التصنيف" to category(e.z).ar,
        "الدورة" to "${period(e.z)}",
        "المجموعة" to group(e.z),
        "الحالة (عند 25°م)" to state(e.z),
        "البروتونات / الإلكترونات" to "${e.z}",
        "النيوترونات (تقريبي)" to "$neutrons",
        "التوزيع الإلكتروني" to electronConfig(e.z)
    )
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButton(onDismiss) { Text("إغلاق") } },
        title = { Text("${e.ar} (${e.symbol})", fontWeight = FontWeight.Bold) },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                rows.forEach { (k, v) ->
                    Row(Modifier.fillMaxWidth().padding(vertical = 5.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(k, color = Color.Gray)
                        Text(v, fontWeight = FontWeight.Medium)
                    }
                    HorizontalDivider()
                }
            }
        }
    )
}
