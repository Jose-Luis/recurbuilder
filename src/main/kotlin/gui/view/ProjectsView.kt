package gui.view

import gui.controller.ProjectsController
import javafx.beans.binding.Bindings
import node.Node
import tornadofx.*
import javax.security.auth.callback.Callback
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty1

fun Node.isDirty(): Boolean {
    return this.hasFlag("dirty")
}


class ProjectsView : View("My View") {
    private val projectsController: ProjectsController by inject()
    override val root = vbox {
        projectsController.projects.nodes().forEach { it.flag("dirty") }
        tableview(projectsController.projects.nodes()) {
            readonlyColumn("ID", Node::name)
            readonlyColumn<Node, Boolean>("Parent name", isDirty )
        }
    }
}

val isDirty: KProperty1<Node, Boolean> = { it.hasFlag("dirty") }
