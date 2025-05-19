package view.windows

import androidx.compose.material.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import view.windows.graphWindow
import javax.swing.JFileChooser
import javax.swing.UIManager
import javax.swing.filechooser.FileNameExtensionFilter

@Composable
fun materialFileOpener(
    shouldShow: MutableState<Boolean>,
    extension: String,
) {
    if (shouldShow.value) {
        AlertDialog(
            onDismissRequest = { shouldShow.value = false },
            buttons = {},
            text = {
                val lookAndFeel = mdlaf.MaterialLookAndFeel()
                UIManager.setLookAndFeel(lookAndFeel)
                val fileChooser = JFileChooser()
                val fileFilter = FileNameExtensionFilter(null, extension)
                fileChooser.addChoosableFileFilter(fileFilter)
                val option = fileChooser.showOpenDialog(null)
                if (option == JFileChooser.CANCEL_OPTION) {
                    shouldShow.value = false
                }
            },
        )
    }
}

@Composable
fun fileKitOpener(
    shouldShow: MutableState<Boolean>,
    extension: String,
    showParentWindow: MutableState<Boolean>,
    showGraph: MutableState<Boolean>,
) {
    if (shouldShow.value) {
        var file: MutableState<PlatformFile> =
            remember {
                mutableStateOf(PlatformFile(""))
            }
        val launcher =
            rememberFilePickerLauncher(
                type = FileKitType.File(extension),
                mode = FileKitMode.Single,
                title = "Выберите .$extension файл",
                onResult = { it ->
                    if (it != null) {
                        file.value = it
                        showParentWindow.value = false
                        showGraph.value = true
                        shouldShow.value = false
                    }
                },
            )
        launcher.launch()
    }
}
