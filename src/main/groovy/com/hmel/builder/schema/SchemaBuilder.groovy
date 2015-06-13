package com.hmel.builder.schema;

import groovy.sql.Sql;
import groovy.io.FileType;

class SchemaBuilder {
	
	private def getSqlFile(def email) {
		def lst = []
		def dir = new File("sql-files/")
		dir.eachFileRecurse(FileType.FILES) {file ->
			if(file.name.endsWith(".sql")) {
				def flag = false
				file.eachLine() { line ->
					if(line.toString().contains(email))
						flag = true
				}
				if(!flag)
					lst << file
				else 
					println "${file.name} was runned before and skipped!"
			}
		}
	return lst
	}

	public def update() {
		def sql = Sql.newInstance("jdbc:mysql://localhost:3306/", "root", "", "com.mysql.jdbc.Driver")
		def email = ""
		"git config --local user.email".execute().text.eachLine() { email+= it}
		def sqlComment = "#"
		getSqlFile(email).sort{it.name}.each { sqlSyntax ->
			println "Running ${sqlSyntax} script..."
			String query = ""
			sqlSyntax.eachLine() { line ->
				if(line?.trim() && !line.startsWith(sqlComment))
					query += line.trim()
				if(query?.trim() && query.endsWith(";")) {
					println "\t${query}...\n"
					sql.execute(query)
					query = ""
				}
			}
			sqlSyntax.append "\n${sqlCOmment}${email}"
		}
	}	
}
