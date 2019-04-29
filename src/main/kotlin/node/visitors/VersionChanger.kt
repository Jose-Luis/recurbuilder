package node.visitors

import info.Info
import node.Node
import org.apache.maven.model.Model
import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import org.apache.maven.model.io.xpp3.MavenXpp3Writer
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class VersionChanger(val info: Info, val version: String) : NodeVisitor {
    override fun visit(node: Node) {
        val projectDir = info.workspace.resolve(node.dir)
        setVersion(projectDir, version)
        getModel(projectDir).modules.forEach { setParentVersion(projectDir.resolve(it), version) }
    }

    private fun getModel(projectDir: File): Model {
        val pom = projectDir.resolve("pom.xml")
        return MavenXpp3Reader().read(FileReader(pom))
    }

    private fun setVersion(projectDir: File, version: String) {
        val model = getModel(projectDir)
        model.version = version
        MavenXpp3Writer().write(FileWriter(projectDir.resolve("pom.xml")), model)
    }

    private fun setParentVersion(projectDir: File, version: String) {
        val model = getModel(projectDir)
        model.parent.version = version
        MavenXpp3Writer().write(FileWriter(projectDir.resolve("pom.xml")), model)
    }
}