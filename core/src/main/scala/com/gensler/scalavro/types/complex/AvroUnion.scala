package com.gensler.scalavro.types.complex

import com.gensler.scalavro.types.{AvroType, AvroComplexType}
import com.gensler.scalavro.types.primitive.AvroNull
import com.gensler.scalavro.JsonSchemaProtocol._

import scala.reflect.runtime.universe._
import scala.util.{Try, Success, Failure}

import spray.json._

import java.io.{InputStream, OutputStream}

class AvroUnion[A: TypeTag, B: TypeTag] extends AvroComplexType[Either[A, B]] {

  type LeftType = A
  type RightType = B

  val typeName = "union"

  def write(obj: Either[LeftType, RightType], stream: OutputStream) = obj match {
    case Left(a)  => ???
    case Right(b) => ???
  }

  def writeAsJson(obj: Either[LeftType, RightType]): JsValue = obj match {
    case Left(a)  => ???
    case Right(b) => ???
  }

  def read(stream: InputStream) = Try { ???.asInstanceOf[Either[A, B]] }

  def readFromJson(json: JsValue) = Try { ???.asInstanceOf[Either[A, B]] }

  override def schema() = Set(
    typeSchemaOrNull[LeftType],
    typeSchemaOrNull[RightType]
  ).toJson

  override def parsingCanonicalForm(): JsValue = {
    Set(
      AvroType.fromType[LeftType].map { _.canonicalFormOrFullyQualifiedName } getOrElse AvroNull.schema,
      AvroType.fromType[RightType].map { _.canonicalFormOrFullyQualifiedName } getOrElse AvroNull.schema
    ).toJson
  }

  def dependsOn(thatType: AvroType[_]) = {
    (AvroType.fromType[LeftType], AvroType.fromType[RightType]) match {
      case (Success(leftAvroType), Success(rightAvroType)) => {
        leftAvroType == thatType || rightAvroType == thatType ||
        (leftAvroType dependsOn thatType) || (rightAvroType dependsOn thatType)
      }
      case _ => false
    }
  }

}