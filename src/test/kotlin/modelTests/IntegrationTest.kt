package modelTests

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import viewmodel.GraphVM
import viewmodel.GwButtonType
import java.io.File

class IntegrationTest {
    private val testFilePath = "integration_test_graph.json"

    @AfterEach
    fun tearDown() {
        val file = File(testFilePath)
        if (file.exists()) file.delete()
    }

    @Test
    fun `full scenario load json select vertices and run bellman ford`() {
        val jsonContent =
            """
            {
              "isDirected": true,
              "isWeighted": true,
              "vertices": [
                { "id": "A", "x": 0.0, "y": 0.0 },
                { "id": "B", "x": 10.0, "y": 0.0 },
                { "id": "C", "x": 20.0, "y": 0.0 }
              ],
              "edges": [
                { "from": "A", "to": "B", "weight": 2.0 },
                { "from": "B", "to": "C", "weight": 3.0 },
                { "from": "A", "to": "C", "weight": 10.0 }
              ]
            }
            """.trimIndent()
        File(testFilePath).writeText(jsonContent)
        val vm =
            GraphVM(
                readFrom = testFilePath,
                extension = "json",
                error = mutableStateOf(false),
                errorMessage = mutableStateOf(""),
                askInput = mutableStateOf(false),
                loadFile = mutableStateOf(""),
                showResult = mutableStateOf(false),
                resultMessage = mutableStateOf(""),
                result = mutableStateOf(0.0),
            )
        assertEquals(3, vm.graph.vertices.size)
        assertEquals(3, vm.e.size)
        val vertexA = vm.v.values.find { it.label == "A" }!!
        val vertexC = vm.v.values.find { it.label == "C" }!!
        vertexA.selected.value = true
        vertexC.selected.value = true
        vm.execute(GwButtonType.BELLMAN_FORD)
        assertTrue(vm.showResult.value)
        assertEquals(5.0, vm.result.value, 0.001)
        assertEquals(Color.Red, vertexA.color.value)
        assertEquals(Color.Red, vertexC.color.value)
        val vertexB = vm.v.values.find { it.label == "B" }!!
        assertEquals(Color.Red, vertexB.color.value)
    }
}
