package doobie.util

import shapeless._, shapeless.test._
import doobie.imports._
import org.specs2.mutable.Specification

object compositespec extends Specification {

  case class Woozle(a: (String, Int), b: Int :: String :: HNil, c: Boolean)

  case class LenStr1(n: Int, s: String)

  case class LenStr2(n: Int, s: String)
  object LenStr2 {
    implicit val LenStrMeta = 
      Meta[String].nxmap[LenStr2](s => LenStr2(s.length, s), _.s)
  }

  "Composite" should { 

    "exist for some fancy types" in {
      Composite[Int]
      Composite[(Int, Int)]
      Composite[(Int, Int, String)]
      Composite[(Int, (Int, String))]
      Composite[Woozle]

      // https://github.com/tpolecat/doobie/pull/126 was reverted because these
      // derivations were failing with SOE
      Composite[(Woozle, String)]
      Composite[(Int, Woozle :: Woozle :: String :: HNil)]

      true
    }

    "select multi-column instance by default" in {
      Composite[LenStr1].length must_== 2
    }

    "select 1-column instance when available" in {
      Composite[LenStr2].length must_== 1
    }

  }

}
