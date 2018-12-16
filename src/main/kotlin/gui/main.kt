package gui

import gui.view.LoadProjectsView
import tornadofx.*

class BuilderGui : App(LoadProjectsView::class)

fun main(args: Array<String>) {
    launch<BuilderGui>(args)
}