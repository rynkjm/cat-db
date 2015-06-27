package controllers

import javax.inject.Inject
import dao.CatDao
import play.api.mvc._
import models.Cat
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.concurrent.Execution.Implicits.defaultContext



class Application @Inject()(catDao: CatDao) extends Controller {

  val catForm = Form(
    mapping(
      "name" -> text(),
      "color" -> text()
    )(Cat.apply)(Cat.unapply)
  )

  def index = Action.async {
    catDao.all().map(res => Ok(views.html.index(res.toList)))
  }

  def insert = Action.async { implicit request =>
    val cat: Cat = catForm.bindFromRequest.get
    catDao.insert(cat).map(_ => Redirect(routes.Application.index))
  }

}
