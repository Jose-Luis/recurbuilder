package node.visitors

import info.Info
import node.Node

class PomVersionChanger(val info: Info, val nodename: String, val version: String) : NodeVisitor {
    override fun visit(node: Node) {
        val versionTagRegex = Regex("(.*<\\w*\\.?$nodename.version>)(.*)(</\\w*\\.?$nodename.version>)")
        info.workspace.resolve(node.dir).walkTopDown()
            .filter { it.name.contains("pom.xml") && it.readText().contains(versionTagRegex) }
            .forEach {
                val tempFile = createTempFile("pom.xml.$nodename")
                tempFile.printWriter().use { writer ->
                    it.forEachLine { line ->
                        writer.println(
                            if (line.contains(versionTagRegex)) {
                                System.out.println("${nodename.toUpperCase()} --> Setting dreamliner version on ${it.absolutePath}")
                                System.out.println("OLD: $line")
                                val (open, oldversion, close) = versionTagRegex.find(line)!!.destructured
                                System.out.println("NEW: $open$version$close")
                                "$open$version$close"
                            } else line
                        )
                    }
                }
                check(it.delete() && tempFile.renameTo(it)) { "failed to replace file" }
            }
    }

}