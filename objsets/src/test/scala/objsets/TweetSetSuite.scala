package objsets

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TweetSetSuite extends FunSuite {
  trait TestSets {
    val set1 = new Empty
    val a = new Tweet("a", "a body", 20)
    val b = new Tweet("b", "b body", 20)
    val set2 = set1.incl(a) // a
    val set3 = set2.incl(b) // a, b
    val c = new Tweet("c", "c body", 7) // Tweet c
    val d = new Tweet("d", "d body", 9) // Tweet d
    val e = new Tweet("e", "e body", 15) // Tweet e
    val f = new Tweet("f", "f body", 22) // Tweet f
    val g = new Tweet("g", "g body", 19) // Tweet g
    val setc = set1.incl(c) // c
    val setd = set1.incl(d) // d
    val set4c = set3.incl(c) // a, b, c
    val set4d = set3.incl(d) // a, b, d
    val set5 = set4c.incl(d) // a, b, c, d
    val acd = set2.incl(c).incl(d) // a, c, d
    val acdefg = set2.incl(d).incl(e).incl(g).incl(c).incl(f) // a, c, d, e, f, g
  }

  def asSet(tweets: TweetSet): Set[Tweet] = {
    var res = Set[Tweet]()
    tweets.foreach(res += _)
    res
  }

  def size(set: TweetSet): Int = asSet(set).size

  test("filter: on empty set") {
    new TestSets {
      assert(size(set1.filter(tw => tw.user == "a")) === 0)
    }
  }

  test("filter: a on set5") {
    new TestSets {
      assert(size(set5.filter(tw => tw.user == "a")) === 1)
    }
  }

  test("filter: b on set5") {
    new TestSets {
      assert(size(set5.filter(tw => tw.user == "b")) === 1)
    }
  }

  test("filter: c on set5") {
    new TestSets {
      assert(size(set5.filter(tw => tw.user == "c")) === 1)
    }
  }

  test("filter: c || b on set5") {
    new TestSets {
      assert(size(set5.filter(tw => tw.user == "c" || tw.user == "b")) === 2)
    }
  }

  test("filter: d on set5") {
    new TestSets {
      assert(size(set5.filter(tw => tw.user == "d")) === 1)
    }
  }

  test("filter: a on set4d") {
    new TestSets {
      assert(size(set4d.filter(tw => tw.user == "a")) === 1)
    }
  }

  test("filter: b on set4d") {
    new TestSets {
      assert(size(set4d.filter(tw => tw.user == "b")) === 1)
    }
  }

  test("filter: d on set4d") {
    new TestSets {
      assert(size(set4d.filter(tw => tw.user == "d")) === 1)
    }
  }

  test("filter: c on set4d is false") {
    new TestSets {
      assert(size(set4d.filter(tw => tw.user == "c")) === 0)
    }
  }

  test("filter: set4d is the same filtering with p => true") {
    new TestSets {
      assert(size(set4d.filter(tw => true)) === 3)
    }
  }

  test("filter: set4c is the same filtering with p => true") {
    new TestSets {
      assert(size(set4c.filter(tw => true)) === 3)
    }
  }

  test("filter: set5 is the same filtering with p => true") {
    new TestSets {
      assert(size(set5.filter(tw => true)) === 4)
    }
  }

  test("filter: 20 on set5") {
    new TestSets {
      assert(size(set5.filter(tw => tw.retweets == 20)) === 2)
    }
  }

  test("union: set4c and setd") {
    new TestSets {
      assert(size(setd.union(set4c)) === 4)
    }
  }

  test("union: set4c and set4d") {
    new TestSets {
      assert(size(set4c.union(set4d)) === 4)
    }
  }

  test("union: with empty set (1)") {
    new TestSets {
      assert(size(set5.union(set1)) === 4)
    }
  }

  test("union: with empty set (2)") {
    new TestSets {
      assert(size(set1.union(set5)) === 4)
    }
  }

  test("mostRetweeted: set5") {
    new TestSets {
      val mrSet = (new Empty).incl(set5.mostRetweeted)
      assert(size(mrSet) === 1)
      assert(size(mrSet.filter(tw => tw.retweets == 20)) === 1)
    }
  }

  test("descending: set5") {
    new TestSets {
      val trends = set5.descendingByRetweet
      assert(!trends.isEmpty)
      assert(trends.head.user == "a" || trends.head.user == "b")
    }
  }

  test("descending: acd") {
    new TestSets {
      val trends = acd.descendingByRetweet
      assert(!trends.isEmpty)
      assert(trends.head.user == "a")
      assert(trends.tail.head.user == "d")
      assert(trends.tail.tail.head.user == "c")
    }
  }
  
    test("descending: acdefg") {
    new TestSets {
      val trends = acdefg.descendingByRetweet
      assert(!trends.isEmpty)
      assert(trends.head.user == "f")
      assert(trends.tail.head.user == "a")
      assert(trends.tail.tail.head.user == "g")
      assert(trends.tail.tail.tail.head.user == "e")
      assert(trends.tail.tail.tail.tail.head.user == "d")
      assert(trends.tail.tail.tail.tail.tail.head.user == "c")
    }
  }
  
  test("googleTweets") {
    new TestSets {
      val google = List("android", "Android", "galaxy", "Galaxy", "nexus", "Nexus")
      lazy val googleTweets: TweetSet = TweetReader.allTweets.filter((t: Tweet) => google.exists((w: String) => t.text.contains(w)))
          
      assert(size(googleTweets) === 38)
    }
  }
  
  test("appleTweets") {
    new TestSets {
      val apple = List("ios", "iOS", "iphone", "iPhone", "ipad", "iPad")
      lazy val appleTweets: TweetSet = TweetReader.allTweets.filter((t: Tweet) => apple.exists((w: String) => t.text.contains(w)))
          
      assert(size(appleTweets) === 150)
    }
  }
  
  test("trending") {
    new TestSets {
      val google = List("android", "Android", "galaxy", "Galaxy", "nexus", "Nexus")
      lazy val googleTweets: TweetSet = TweetReader.allTweets.filter((t: Tweet) => google.exists((w: String) => t.text.contains(w)))
      
      val apple = List("ios", "iOS", "iphone", "iPhone", "ipad", "iPad")
      lazy val appleTweets: TweetSet = TweetReader.allTweets.filter((t: Tweet) => apple.exists((w: String) => t.text.contains(w)))
    
      lazy val trending: TweetList = (googleTweets union appleTweets) descendingByRetweet
      
      assert(!trending.isEmpty)
      assert(trending.head.retweets  == 321)
    }
  }
  
}
