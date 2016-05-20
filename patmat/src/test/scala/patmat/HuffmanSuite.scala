package patmat

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import patmat.Huffman._

@RunWith(classOf[JUnitRunner])
class HuffmanSuite extends FunSuite {
  trait TestTrees {
    val t1 = Fork(Leaf('a',2), Leaf('b',3), List('a','b'), 5)
    val t2 = Fork(Fork(Leaf('a',2), Leaf('b',3), List('a','b'), 5), Leaf('d',4), List('a','b','d'), 9)
  }

  test("weight of a larger tree") {
    new TestTrees {
      assert(weight(t1) === 5)
    }
  }

  test("chars of a larger tree") {
    new TestTrees {
      assert(chars(t2) === List('a','b','d'))
    }
  }
  
  test("string2chars(\"hello, world\")") {
    assert(string2Chars("hello, world") === List('h', 'e', 'l', 'l', 'o', ',', ' ', 'w', 'o', 'r', 'l', 'd'))
  }
  
  test("times") {
    assert(times(string2Chars("aba")) === List(('a',2),('b',1)))
    assert(times(string2Chars("hello, world")) === List(('h',1),('e',1),('l',3),('o',2),(',',1),(' ',1),('w',1),('r',1),('d',1)))
  }

  test("makeOrderedLeafList for some frequency table") {
    assert(makeOrderedLeafList(List(('t', 2), ('e', 1), ('x', 3))) === List(Leaf('e',1), Leaf('t',2), Leaf('x',3)))
  }

  test("combine of some leaf list") {
    val leaflist = List(Leaf('e', 1), Leaf('t', 2), Leaf('x', 4))
    assert(combine(leaflist) === List(Fork(Leaf('e',1),Leaf('t',2),List('e', 't'),3), Leaf('x',4)))
    
    val leaflist2 = List(Leaf('e', 2), Leaf('t', 3), Leaf('x', 4))
    assert(combine(leaflist2) === List(Leaf('x',4), Fork(Leaf('e',2),Leaf('t',3),List('e', 't'),5)))
  }
  
  test("combine of singleton or Nil") {
    val leaflist = List(Leaf('e', 1))
    assert(combine(leaflist) === List(Leaf('e', 1)))
    assert(combine(Nil) === Nil)
  }
  
  test("until of some leaf list") {
    val leaflist = List(Leaf('e', 1), Leaf('t', 2), Leaf('x', 4))
    assert(until(singleton,combine)(leaflist) === Fork(Fork(Leaf('e',1),Leaf('t',2),List('e', 't'),3), Leaf('x',4),List('e','t','x'),7))
  }
  
  test("createCodeTree") {
    val leaflist = List(Leaf('e', 1), Leaf('t', 2), Leaf('x', 4))
    assert(createCodeTree(string2Chars("aba")) === Fork(Leaf('b',1),Leaf('a',2),List('b', 'a'),3))
  }
  
  test("decode secret") {
    println( "the decoded secret is: " + decodedSecret )
  }

  test("decode and encode a very short text should be identity") {
    new TestTrees {
      assert(decode(t1, encode(t1)("ab".toList)) === "ab".toList)
      assert(decode(t2, encode(t2)("dbaabddbdaa".toList)) === "dbaabddbdaa".toList)
    }
  }
  
  test("decode and quickEncode a very short text should be identity") {
    new TestTrees {
      assert(decode(t1, quickEncode(t1)("ab".toList)) === "ab".toList)
      assert(decode(t2, quickEncode(t2)("dbaabddbdaa".toList)) === "dbaabddbdaa".toList)
    }
  }
}
