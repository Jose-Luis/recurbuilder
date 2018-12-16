package gui.view

import gui.controller.ProjectsController
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import node.ProjectTree
import tornadofx.*

class LoadProjectsView : View() {

    val projectsController: ProjectsController by inject()

    private val ef = arrayOf(FileChooser.ExtensionFilter("Projects file", "projects.json"))

    override val root = VBox()

    init {
        with(root) {
            button("Target Directory") {
                action {
                    val file = chooseFile("Select projects file", ef, FileChooserMode.Single)
                    projectsController.projects = ProjectTree(file.first())
                    replaceWith<ProjectsView>()
                }
            }
        }
    }
}