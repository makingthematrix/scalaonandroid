package comments.cloud

import com.gluonhq.cloudlink.client.data.{DataClient, DataClientBuilder}
import com.gluonhq.connect.ConnectState
import com.gluonhq.connect.provider.DataProvider
import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections

import javax.annotation.PostConstruct

import comments.model.Comment

class Service {
  private val CLOUD_LIST_ID = "comments"

  private var dataClient: DataClient = _

  val commentsList = new SimpleListProperty[Comment](FXCollections.observableArrayList())

  @PostConstruct
  def postConstruct(): Unit = {
    dataClient = DataClientBuilder.create().build()
  }

  def retrieveComments(): Unit = {
    val retrieveList =
      DataProvider.retrieveList(dataClient.createListDataReader(CLOUD_LIST_ID, classOf[Comment]))

    retrieveList
      .stateProperty().addListener((_, _, state) =>
      if (ConnectState.SUCCEEDED.equals(state))
        commentsList.set(retrieveList)
    )
  }

  def addComment(comment: Comment): Boolean = commentsList.get.add(comment)
}
