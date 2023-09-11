package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import java.nio.file.Paths
import models._
import java.util.UUID
import play.api.data.Forms._
import play.api.data.Form
import org.checkerframework.checker.i18nformatter.qual.I18nFormat
import play.api.i18n.I18nSupport
import akka.protobufv3.internal.compiler.PluginProtos.CodeGeneratorResponse.File

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController with I18nSupport{

 

  val fileList = FileModel.file

  def fileForm = Form(
    mapping(
      "id" -> default(uuid, UUID.randomUUID()),
      "fileName" -> ignored(""),
    )(FileModel.apply)(FileModel.unapply)
  )
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index(fileList.toList, fileForm))
  }

  def upload() = Action(parse.multipartFormData) { implicit request =>
    fileForm.bindFromRequest().fold(
      formWithError => {
        BadRequest("Something went wrong.")
      },
      files => {
        request.body
        .file("file")
        .map { file =>
          val filename    = Paths.get(file.filename).getFileName
          val fileSize    = file.fileSize
          val contentType = file.contentType
        
          file.ref.copyTo(Paths.get(s"public/files/$filename"), replace = true)
          FileModel.uploadFile(FileModel(UUID.randomUUID(), s"$filename"))
          Redirect(routes.HomeController.index())
        }
        .getOrElse {
          Redirect(routes.HomeController.index())
        }
      }
    )
  }

  // def download(id: UUID) = Action { implicit request =>
  //   fileForm.bindFromRequest().fold(
  //     formWithError => {
  //       BadRequest("Something went wwwwrong")
  //     },
  //     downloadFile => {
  //       val f = FileModel.file.find(_.id == id)
  //       f match {
  //         case Some(value) => Redirect(routes.Assets.versioned(s"files/${downloadFile.fileName}"))
  //         case None => BadRequest("Yep, something wernt wrong")
  //       }
  //     }
  //   )
  // }

  def delete(id: UUID) = Action { implicit request =>
      FileModel.deleteFile(id)
      Redirect(routes.HomeController.index())
  }

  
}
