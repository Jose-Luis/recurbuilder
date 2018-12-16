package gui.controller

import javafx.beans.property.ReadOnlyProperty
import node.Node
import node.ProjectTree
import tornadofx.*

class ProjectsController: Controller() {
    lateinit var projects: ProjectTree
}