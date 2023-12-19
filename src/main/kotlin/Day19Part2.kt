import structure.LinesPuzzle

class Day19Part2 : LinesPuzzle() {
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

        val startCriteria = ItemCriteria(
            mapOf(
                'x' to Pair(0,4001),
                'm' to Pair(0,4001),
                'a' to Pair(0,4001),
                's' to Pair(0,4001),
                )
        )

        val combinations = getCombinations(startWorkflow, workflows, startCriteria)

        return combinations.sumOf { it.size }.toString()
    }

    fun getCombinations(currentWorkflow : String, workflows : Map<String,Workflow>, criteria : ItemCriteria) : List<ItemCriteria> {
        if(currentWorkflow == "A"){
            return listOf(criteria)
        }else if( currentWorkflow == "R"){
            return emptyList()
        }
        else {
            val conditionalWorkflows = workflows.getValue(currentWorkflow).getConditionalWorkflows(criteria)
            return conditionalWorkflows.flatMap {
                getCombinations(it.second,
                workflows,
                it.first)
            }
        }
    }

    companion object {

        data class ItemCriteria(val criteria : Map<Char,Pair<Int,Int>>){
            val containsValues get() = size > 1
            val size get() = criteria.map { (it.value.second - it.value.first - 1).toLong() }.reduce { acc, i -> acc * i }

            fun addCondition (condition : Condition) : ItemCriteria{
                var (min, max) = criteria.getValue(condition.property)
                when (condition.operator) {
                    '<' -> { // condition.testValue is a upper bound
                        max = Math.min(condition.testValue,max)
                    }
                    '>' -> { // condition.testValue is a lower bound
                        min = Math.max(condition.testValue, min)
                    }
                    else -> throw RuntimeException("invalid condition operator")
                }
                val new = criteria.toMutableMap()
                new[condition.property] = Pair(min,max)
                return ItemCriteria(new)
            }
        }

        data class Workflow(val name: String, val conditions: List<Condition>, val defaultNext: String) {

            fun workItem(item: Item): String {
                return conditions.firstOrNull { it.matchItem(item) }?.nextWorkflow ?: defaultNext
            }

            fun getConditionalWorkflows(itemCriteria : ItemCriteria) : List<Pair<ItemCriteria,String>>{
                var criteria = itemCriteria
                val list = mutableListOf<Pair<ItemCriteria,String>>()
                conditions.forEach {
                    list.add(Pair(criteria.addCondition(it), it.nextWorkflow))
                    criteria = criteria.addCondition(it.getNegated())
                }
                list.add(Pair(criteria,defaultNext))
                return list.filter { it.first.containsValues }
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
            val property: Char,
            val operator: Char,
            val testValue: Int,
            val nextWorkflow: String
        ) {

            fun getNegated() : Condition{
                val newTest =  when (operator) {
                    '<' -> Pair('>',testValue - 1)
                    '>' -> Pair('<',testValue + 1)
                    else -> throw RuntimeException("invalid condition operator $operator")
                }
                return Condition(property,newTest.first,newTest.second,"!$nextWorkflow")
            }

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