//: ----------------------------------------------------------------------------
//: Copyright (C) 2016 Verizon.  All Rights Reserved.
//:
//:   Licensed under the Apache License, Version 2.0 (the "License");
//:   you may not use this file except in compliance with the License.
//:   You may obtain a copy of the License at
//:
//:       http://www.apache.org/licenses/LICENSE-2.0
//:
//:   Unless required by applicable law or agreed to in writing, software
//:   distributed under the License is distributed on an "AS IS" BASIS,
//:   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//:   See the License for the specific language governing permissions and
//:   limitations under the License.
//:
//: ----------------------------------------------------------------------------
package verizon.build

import sbt._, Keys._

object PromptPlugin extends AutoPlugin {
  override def trigger = allRequirements

  override lazy val projectSettings = Seq(
    shellPrompt := {
    state: State =>
      val extracted = Project.extract(state)
      import extracted._
      //get name of current project and construct prompt string
      (name in currentRef get structure.data).map {
        name => "[" + cyan(name) + "] λ "
      }.getOrElse(red("> "))
    }
  )

  //////////////////////// INTERNALS ////////////////////////

  import scala.Console._

  lazy val isANSISupported = {
    Option(System.getProperty("sbt.log.noformat")).map(_ != "true").orElse {
      Option(System.getProperty("os.name"))
        .map(_.toLowerCase)
        .filter(_.contains("windows"))
        .map(_ => false)
    }.getOrElse(true)
  }

  def red(str: String): String =
    if(isANSISupported) RED + str + RESET
    else str

  def cyan(str: String) =
    if (isANSISupported) (CYAN + str + RESET)
    else str
}



