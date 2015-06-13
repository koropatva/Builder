package com.hmel.builder;

import org.gradle.api.*;

class BuilderPlugin implements Plugin<Project> {
	def void apply(Project project) {
		project.task('hello') << {
			println 'Hi all from Builder project'
		}
	}
}
