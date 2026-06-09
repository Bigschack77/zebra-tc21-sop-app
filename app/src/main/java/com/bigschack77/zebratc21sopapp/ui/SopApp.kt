package com.bigschack77.zebratc21sopapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bigschack77.zebratc21sopapp.data.model.Sop
import com.bigschack77.zebratc21sopapp.data.model.Step
import com.bigschack77.zebratc21sopapp.data.repository.SopRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

private object SopRoutes {
    const val list = "sopList"
    const val detail = "sopDetail/{sopId}"
    const val stepEditor = "stepEditor/{sopId}?stepId={stepId}"

    fun detail(sopId: String) = "sopDetail/$sopId"

    fun stepEditor(sopId: String, stepId: String? = null) =
        if (stepId == null) {
            "stepEditor/$sopId"
        } else {
            "stepEditor/$sopId?stepId=$stepId"
        }
}

@Composable
fun SopApp(repository: SopRepository) {
    val navController = rememberNavController()
    val sopViewModel: SopViewModel = viewModel(factory = SopViewModel.factory(repository))
    val sops by sopViewModel.sops.collectAsState()

    MaterialTheme {
        NavHost(navController = navController, startDestination = SopRoutes.list) {
            composable(SopRoutes.list) {
                SopListScreen(
                    sops = sops,
                    onCreateSop = { title ->
                        val sopId = sopViewModel.createSop(title)
                        navController.navigate(SopRoutes.detail(sopId))
                    },
                    onOpenSop = { sopId -> navController.navigate(SopRoutes.detail(sopId)) },
                )
            }
            composable(
                route = SopRoutes.detail,
                arguments = listOf(navArgument("sopId") { type = NavType.StringType }),
            ) { backStackEntry ->
                val sopId = backStackEntry.arguments?.getString("sopId").orEmpty()
                val sop = sops.firstOrNull { it.id == sopId }
                SopDetailScreen(
                    sop = sop,
                    onBack = { navController.popBackStack() },
                    onSaveTitle = { title -> sopViewModel.updateSopTitle(sopId, title) },
                    onAddStep = { navController.navigate(SopRoutes.stepEditor(sopId)) },
                    onEditStep = { stepId -> navController.navigate(SopRoutes.stepEditor(sopId, stepId)) },
                )
            }
            composable(
                route = SopRoutes.stepEditor,
                arguments = listOf(
                    navArgument("sopId") { type = NavType.StringType },
                    navArgument("stepId") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    },
                ),
            ) { backStackEntry ->
                val sopId = backStackEntry.arguments?.getString("sopId").orEmpty()
                val stepId = backStackEntry.arguments?.getString("stepId")
                val sop = sops.firstOrNull { it.id == sopId }
                val step = sop?.steps?.firstOrNull { it.id == stepId }
                StepEditorScreen(
                    sop = sop,
                    existingStep = step,
                    onBack = { navController.popBackStack() },
                    onSave = { title, description, imagePaths ->
                        sopViewModel.saveStep(
                            sopId = sopId,
                            stepId = stepId,
                            title = title,
                            description = description,
                            imagePaths = imagePaths,
                        )
                        navController.popBackStack()
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SopListScreen(
    sops: List<Sop>,
    onCreateSop: (String) -> Unit,
    onOpenSop: (String) -> Unit,
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var newSopTitle by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("SOP-liste") })
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showDialog = true },
                text = { Text("Ny SOP") },
            )
        },
    ) { innerPadding ->
        if (sops.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Ingen SOP'er endnu",
                    style = MaterialTheme.typography.headlineSmall,
                )
                Text(
                    text = "Tryk på 'Ny SOP' for at oprette den første SOP.",
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        Text(
                            text = "SOP-oversigt",
                            style = MaterialTheme.typography.headlineSmall,
                        )
                        Text("MVP-skelet med lokal in-memory lagring.")
                    }
                }
                items(sops, key = { it.id }) { sop ->
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .clickable { onOpenSop(sop.id) },
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = sop.title, fontWeight = FontWeight.Bold)
                            Text(text = "Oprettet: ${formatDate(sop.createdAt)}")
                            Text(text = "Trin: ${sop.steps.size}")
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Opret SOP") },
            text = {
                OutlinedTextField(
                    value = newSopTitle,
                    onValueChange = { newSopTitle = it },
                    label = { Text("Titel") },
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onCreateSop(newSopTitle)
                        newSopTitle = ""
                        showDialog = false
                    },
                ) {
                    Text("Opret")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        newSopTitle = ""
                        showDialog = false
                    },
                ) {
                    Text("Annuller")
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SopDetailScreen(
    sop: Sop?,
    onBack: () -> Unit,
    onSaveTitle: (String) -> Unit,
    onAddStep: () -> Unit,
    onEditStep: (String) -> Unit,
) {
    var title by rememberSaveable(sop?.id) { mutableStateOf(sop?.title.orEmpty()) }

    LaunchedEffect(sop?.title) {
        title = sop?.title.orEmpty()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SOP-detalje") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Tilbage")
                    }
                },
            )
        },
    ) { innerPadding ->
        if (sop == null) {
            Column(modifier = Modifier.padding(innerPadding).padding(24.dp)) {
                Text("SOP blev ikke fundet.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    Column(modifier = Modifier.padding(16.dp)) {
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("SOP-titel") },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Row(
                            modifier = Modifier.padding(top = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Button(onClick = { onSaveTitle(title) }) {
                                Text("Gem titel")
                            }
                            OutlinedButton(onClick = {}, enabled = false) {
                                Text("Eksport senere")
                            }
                        }
                        Text(
                            text = "Oprettet: ${formatDate(sop.createdAt)}",
                            modifier = Modifier.padding(top = 12.dp),
                        )
                        Text(text = "Antal trin: ${sop.steps.size}")
                        Button(
                            onClick = onAddStep,
                            modifier = Modifier.padding(top = 12.dp),
                        ) {
                            Text("Tilføj trin")
                        }
                    }
                }
                if (sop.steps.isEmpty()) {
                    item {
                        Text(
                            text = "Ingen trin endnu. Tilføj første trin for at bygge SOP'en op.",
                            modifier = Modifier.padding(horizontal = 16.dp),
                        )
                    }
                } else {
                    items(sop.steps, key = { it.id }) { step ->
                        Card(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .clickable { onEditStep(step.id) },
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Trin ${step.stepNumber}: ${step.title}",
                                    fontWeight = FontWeight.Bold,
                                )
                                if (step.description.isNotBlank()) {
                                    Text(
                                        text = step.description,
                                        modifier = Modifier.padding(top = 4.dp),
                                    )
                                }
                                Text(
                                    text = "Billeder: ${step.images.size}",
                                    modifier = Modifier.padding(top = 8.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StepEditorScreen(
    sop: Sop?,
    existingStep: Step?,
    onBack: () -> Unit,
    onSave: (String, String, List<String>) -> Unit,
) {
    var title by rememberSaveable(existingStep?.id) { mutableStateOf(existingStep?.title.orEmpty()) }
    var description by rememberSaveable(existingStep?.id) { mutableStateOf(existingStep?.description.orEmpty()) }
    var imagePaths by rememberSaveable(existingStep?.id) {
        mutableStateOf(existingStep?.images?.map { it.filePath }.orEmpty())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (existingStep == null) {
                            "Opret trin"
                        } else {
                            "Rediger trin"
                        },
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Tilbage")
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = sop?.title ?: "Ukendt SOP",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "Kamera og filvalg er ikke koblet på endnu. Brug billedpladsholdere som MVP-skelet.",
                style = MaterialTheme.typography.bodyMedium,
            )
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Overskrift") },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Beskrivelse") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
            )
            Button(
                onClick = {
                    imagePaths = imagePaths + "billede_${imagePaths.size + 1}.jpg"
                },
            ) {
                Text("Tilføj billed-pladsholder")
            }
            if (imagePaths.isEmpty()) {
                Text("Ingen billeder tilknyttet endnu.")
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    imagePaths.forEachIndexed { index, path ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(text = "Billede ${index + 1}", fontWeight = FontWeight.Bold)
                                Text(text = path)
                                TextButton(onClick = {
                                    imagePaths = imagePaths.filterNot { it == path }.toList()
                                }) {
                                    Text("Fjern")
                                }
                            }
                        }
                    }
                }
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            Button(
                onClick = { onSave(title, description, imagePaths) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Gem trin")
            }
        }
    }
}

private fun formatDate(value: LocalDateTime): String = value.format(dateFormatter)
