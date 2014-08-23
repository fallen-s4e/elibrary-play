package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class ApplicationSpec extends Specification {

  def testPage(path: String) = {
    running(FakeApplication()) {
      val home = route(FakeRequest(GET, path)).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
//      contentAsString(home) must contain ("Your new application is ready.")
    }
  }

  "Application" should {
    
    "send 404 on a bad request" in {
      running(FakeApplication()) {
        route(FakeRequest(GET, "/boum")) must beNone        
      }
    }
    
    "render the index page"    in { testPage("/")                  }
    "render the take1 page"    in { testPage("/take/step1")        }
    "render the take2 page"    in { testPage("/take/step2")        }
    "render the putback page"  in { testPage("/putback")           }
    "render the library page"  in { testPage("/library")           }
    "render the admin page"    in { testPage("/administration")    }
  }
}