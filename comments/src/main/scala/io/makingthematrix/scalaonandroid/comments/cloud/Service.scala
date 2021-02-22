package io.makingthematrix.scalaonandroid.comments.cloud

import com.gluonhq.cloudlink.client.data.{DataClient, DataClientBuilder}
import com.gluonhq.connect.ConnectState
import com.gluonhq.connect.provider.DataProvider
import io.makingthematrix.scalaonandroid.comments.model.Comment
import javafx.beans.property.SimpleListProperty
import javafx.beans.value.ChangeListener
import javafx.collections.FXCollections

import javax.annotation.PostConstruct

class Service {
  private val CLOUD_LIST_ID = "comments"

  private var dataClient: DataClient = _

  val commentsList: SimpleListProperty[Comment] = new SimpleListProperty(FXCollections.observableArrayList())

  @PostConstruct
  def postConstruct(): Unit = {
    dataClient = DataClientBuilder.create().build()
  }

  def retrieveComments(): Unit = {
    val retrieveList =
      DataProvider.retrieveList(dataClient.createListDataReader(CLOUD_LIST_ID, classOf[Comment]))
    retrieveList
      .stateProperty().addListener((_, _, state) => if (state == ConnectState.SUCCEEDED) commentsList.set(retrieveList))
  }

  def addComment(comment: Comment): Boolean = commentsList.get.add(comment)
}
