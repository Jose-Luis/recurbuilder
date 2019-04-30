package node.visitors

import info.Info
import node.Node
import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import org.apache.maven.model.io.xpp3.MavenXpp3Writer
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class ExternalDepVersionChanger(val info: Info, val artifactDescriptor: String) : NodeVisitor {
    override fun visit(node: Node) {
        info.workspace.resolve(node.dir).walkTopDown().filter { it.name.equals("pom.xml") }
            .forEach { setVersion(it, artifactDescriptor) }
    }

    private fun setVersion(pom: File, artifactDescriptor: String) {
        val (groupId, artifactId, version) = artifactDescriptor.split(":")
        val model = MavenXpp3Reader().read(FileReader(pom))
        model.dependencies.filter { it.groupId == groupId && it.artifactId == artifactId && it.version != null }
            .forEach {
                it.version = version
                MavenXpp3Writer().write(FileWriter(pom), model)
            }
    }
}