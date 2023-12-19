import structure.LinesPuzzle

class Day19Part1 : LinesPuzzle() {
    override fun solve(lines: List<String>): String {
        val workflows = mutableMapOf<String, Workflow>()
        val items = mutableListOf<Item>()

        var parseWorkflows = true

        lines.forEach { line ->
            if (line.isEmpty()) {
                parseWorkflows = false
            } else {
                if (parseWorkflows) {
                    val wf = Workflow.fromString(line)
                    workflows[wf.name] = wf
                } else {
                    val item = Item.fromString(line)
                    items.add(item)
                }
            }
        }

        val startWorkflow = "in"

        val accepted = items.filter {
            var currentWorkflow = startWorkflow
            while (currentWorkflow != "A" && currentWorkflow != "R") {
                currentWorkflow = workflows.getValue(currentWorkflow).workItem(it)
            }
            currentWorkflow == "A"
        }

        return accepted.sumOf { it.getValue() }.toString()
    }

    companion object {
        data class Workflow(val name: String, val conditions: List<Condition>, val defaultNext: String) {

            fun workItem(item: Item): String {
                return conditions.firstOrNull { it.matchItem(item) }?.nextWorkflow ?: defaultNext
            }

            companion object {
                fun fromString(string: String): Workflow {
                    val parts = string.substringAfter('{').trim('}').split(',')
                    val conditions = parts.take(parts.size - 1).map { Condition.fromString(it) }
                    return Workflow(
                        string.substringBefore('{'),
                        conditions,
                        parts.last()
                    )
                }
            }
        }

        data class Condition(
            val string: String,
            val property: Char,
            val operator: Char,
            val testValue: Int,
            val nextWorkflow: String
        ) {

            fun matchItem(item: Item): Boolean {
                return when (operator) {
                    '<' -> item.values.getValue(property) < testValue
                    '>' -> item.values.getValue(property) > testValue
                    else -> throw RuntimeException("invalid condition operator $operator")
                }
            }

            companion object {
                fun fromString(string: String): Condition {
                    return Condition(
                        string,
                        string[0],
                        string[1],
                        string.substring(2).substringBefore(':').toInt(),
                        string.substringAfter(':')
                    )
                }
            }
        }

        data class Item(
            val string: String,
            val values: Map<Char, Int>
        ) {
            fun getValue(): Int {
                return values.values.sum()
            }

            companion object {
                fun fromString(string: String): Item {
                    val parts = string.trim('{', '}').split(',').map {
                        val (ch, va) = it.split('=')
                        ch[0] to va.toInt()
                    }.toMap()
                    return Item(string, parts)
                }
            }
        }
    }
}