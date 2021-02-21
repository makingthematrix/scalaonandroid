package io.makingthematrix.scalaonandroid.comments.cloud

import com.gluonhq.cloudlink.client.data.DataClientBuilder
import com.gluonhq.connect.ConnectState
import com.gluonhq.connect.provider.DataProvider
import io.makingthematrix.scalaonandroid.comments.model.Comment
import javafx.beans.property.SimpleListProperty
import javafx.beans.value.ChangeListener
import javafx.collections.FXCollections

class Service {
  private val CLOUD_LIST_ID = "comments"

  val commentsList = new SimpleListProperty(FXCollections.observableArrayList[Comment]())

  lazy private val dataClient = DataClientBuilder.create().build()

  def retrieveComments(): Unit = {
    val retrieveList =
      DataProvider.retrieveList(dataClient.createListDataReader(CLOUD_LIST_ID, classOf[Comment]))
    retrieveList
      .stateProperty().addListener({
        case (_, _, ConnectState.SUCCEEDED) => commentsList.set(retrieveList)
        case _ => ()
      }: ChangeListener[ConnectState])
  }

  def addComment(comment: Comment): Boolean = commentsList.get.add(comment)
}
