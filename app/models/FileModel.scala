package models

import scala.collection.mutable.ListBuffer
import java.util.UUID



case class FileModel(id: UUID, fileName: String)

object FileModel{
    val file: ListBuffer[FileModel] = ListBuffer()

    def uploadFile(newFile: FileModel) = {
        file += newFile
        println(file)
    }

    def deleteFile(fileID: UUID) = {
        val fileToDelete = file.find(_.id == fileID)
        fileToDelete.foreach((f) => file -= f)
    }

    def updateFile(id: UUID) = {

    }

    def findByID(id: UUID) = file.find(_.id == id)


}