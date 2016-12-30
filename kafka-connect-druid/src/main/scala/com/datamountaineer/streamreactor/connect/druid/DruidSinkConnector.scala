/*
 * *
 *   * Copyright 2016 Datamountaineer.
 *   *
 *   * Licensed under the Apache License, Version 2.0 (the "License");
 *   * you may not use this file except in compliance with the License.
 *   * You may obtain a copy of the License at
 *   *
 *   * http://www.apache.org/licenses/LICENSE-2.0
 *   *
 *   * Unless required by applicable law or agreed to in writing, software
 *   * distributed under the License is distributed on an "AS IS" BASIS,
 *   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   * See the License for the specific language governing permissions and
 *   * limitations under the License.
 *   *
 */

package com.datamountaineer.streamreactor.connect.druid

import java.util

import scala.collection.JavaConverters._
import com.typesafe.scalalogging.slf4j.StrictLogging
import org.apache.kafka.connect.connector.Task
import org.apache.kafka.connect.sink.SinkConnector
import com.datamountaineer.streamreactor.connect.druid.config._
import org.apache.kafka.common.config.ConfigDef

/**
  *
  * stream-reactor
  */
class DruidSinkConnector extends SinkConnector with StrictLogging {
  private var configProps: Option[util.Map[String, String]] = None
  private val configDef = DruidSinkConfig.config

  /**
    * States which SinkTask class to use
    **/
  override def taskClass(): Class[_ <: Task] = classOf[DruidSinkTask]

  /**
    * Set the configuration for each work and determine the split
    *
    * @param maxTasks The max number of task workers be can spawn
    * @return a List of configuration properties per worker
    **/
  override def taskConfigs(maxTasks: Int): util.List[util.Map[String, String]] = {
    logger.info(s"Setting task configurations for $maxTasks workers.")
    (1 to maxTasks).map(c => configProps.get).toList.asJava
  }

  /**
    * Start the sink and set to configuration
    *
    * @param props A map of properties for the connector and worker
    **/
  override def start(props: util.Map[String, String]): Unit = {
    logger.info(s"Starting Druid sink task with ${props.toString}.")
    configProps = Some(props)
  }

  override def stop(): Unit = {}

  override def version(): String = getClass.getPackage.getImplementationVersion

  override def config(): ConfigDef = configDef
}
