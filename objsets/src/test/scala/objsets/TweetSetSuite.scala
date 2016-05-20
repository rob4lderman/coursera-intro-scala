package objsets

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TweetSetSuite extends FunSuite {
  trait TestSets {
    val set1 = new Empty
    val set2 = set1.incl(new Tweet("a", "a body", 20))
    val set3 = set2.incl(new Tweet("b", "b body", 20))
    val c = new Tweet("c", "c body", 7)
    val d = new Tweet("d", "d body", 9)
    val set4c = set3.incl(c)
    val set4d = set3.incl(d)
    val set5 = set4c.incl(d)
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

  test("filter: 20 on set5") {
    new TestSets {
      assert(size(set5.filter(tw => tw.retweets == 20)) === 2)
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

  test("most retweeted") {
    new TestSets {
      assert(set5.mostRetweeted.retweets === 20)
      assert(new Empty().incl(c).incl(d).mostRetweeted.retweets === 9)
    }
  }
  
  test("most retweeted on Empty set") {
    intercept[java.util.NoSuchElementException] {
      new Empty().mostRetweeted
    }
  }
  
  test("descending: set5") {
    new TestSets {
      val trends = set5.descendingByRetweet
      assert(!trends.isEmpty)
      assert(trends.head.user == "a" || trends.head.user == "b")
    }
  }
  
  test("descending ") {
    new TestSets {
      val desc = set2.incl(c).incl(d).descendingByRetweet
      assert(desc.head.user == "a" && desc.tail.head.user == "d" && desc.tail.tail.head.user == "c")
      
      val desc2 = set2.incl(d).incl(c).descendingByRetweet
      assert(desc.head.user == "a" && desc.tail.head.user == "d" && desc.tail.tail.head.user == "c")
    }
  }
  
  test("isGoogleTweet / isAppleTweet") {

     assert( GoogleVsApple.isGoogleTweet( new Tweet("a","i got a new galaxy phone!", 10) ) )
     assert( !GoogleVsApple.isAppleTweet( new Tweet("a","i got a new galaxy phone!", 10) ) )
     
     assert( GoogleVsApple.isAppleTweet( new Tweet("a","i got a new ipad!", 10) ) )
     assert( !GoogleVsApple.isGoogleTweet( new Tweet("a","i got a new ipad!", 10) ) )
     
     assert( !GoogleVsApple.isAppleTweet( new Tweet("a","i got a new dell!", 10) ) )
     assert( !GoogleVsApple.isGoogleTweet( new Tweet("a","i got a new dell!", 10) ) )
     
     assert( GoogleVsApple.isAppleTweet( new Tweet("a","i got a new nexus with ios ! HOW??", 10) ) )
     assert( GoogleVsApple.isGoogleTweet( new Tweet("a","i got a new nexus with ios ! HOW??", 10) ) )
  }
  
  test("trending...") {
    
    def f(memo: Int, tw:Tweet) : Int =
      if (tw.retweets > memo) throw new Exception("tweets out of order: " + tw)
      else tw.retweets
    
    GoogleVsApple.trending foldl (10000, f)
    
  }
}
