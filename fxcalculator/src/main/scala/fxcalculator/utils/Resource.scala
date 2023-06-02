package fxcalculator.utils

import java.io.{BufferedInputStream, InputStream}
import java.net.URL
import fxcalculator.Main

import java.nio.file.{Files, Paths}
import Logger.*

import java.nio.charset.StandardCharsets

enum Resource(val value: String):
  case StylesCss          extends Resource("styles.css")
  case ClosePng           extends Resource("close.png")
  case MainFxml           extends Resource("main.fxml")
  case AdvancedEditorFxml extends Resource("advancededitor.fxml")
  case About              extends Resource("about.txt")

object Resource:
  def url(res: Resource): URL = classOf[Main].getResource(res.value)
  def stream(res: Resource): InputStream = classOf[Main].getResourceAsStream(res.value)
  def text(res: Resource): String = new String(stream(res).readAllBytes(), StandardCharsets.UTF_8)
    