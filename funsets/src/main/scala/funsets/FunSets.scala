package funsets

import common._

/**
 * 2. Purely Functional Sets.
 */
object FunSets {
  /**
   * We represent a set by its characteristic function, i.e.
   * its `contains` predicate.
   */
  type Set = Int => Boolean

  /**
   * Indicates whether a set contains a given element.
   */
  def contains(s: Set, elem: Int): Boolean = s(elem)

  /**
   * Returns the set of the one given element.
   */
  def singletonSet(elem: Int): Set = (x:Int) => elem == x

  /**
   * Returns the union of the two given sets,
   * the sets of all elements that are in either `s` or `t`.
   */
  def union(s: Set, t: Set): Set = (x:Int) => contains(s,x) || contains(t,x)

  /**
   * Returns the intersection of the two given sets,
   * the set of all elements that are both in `s` and `t`.
   */
  def intersect(s: Set, t: Set): Set = (x:Int) => contains(s,x) && contains(t,x)

  /**
   * Returns the difference of the two given sets,
   * the set of all elements of `s` that are not in `t`.
   */
  def diff(s: Set, t: Set): Set = (x:Int) => contains(s,x) && !contains(t,x)

  /**
   * Returns the subset of `s` for which `p` holds.
   */
  def filter(s: Set, p: Int => Boolean): Set = (x:Int) => contains(s,x) && p(x)

  /**
   * The bounds for `forall` and `exists` are +/- 1000.
   */
  val bound = 1000

  /**
   * Returns whether all bounded integers within `s` satisfy `p`.
   */
  def forall(s: Set, p: Int => Boolean): Boolean = {
    def iter(a: Int): Boolean = {
      if (a > bound) true	// we got all the way here, it must be true.
      else if (contains(s,a)) p(a) && iter(a+1)	// check p(a) first to stop processing asap
      else iter(a+1)
    }
    iter(-bound)
  }

  /**
   * Returns whether there exists a bounded integer within `s`
   * that satisfies `p`.
   * 
   * The reverse of asking - do all elements in s NOT satisfy p.
   */
  def exists(s: Set, p: Int => Boolean): Boolean = !forall(s, (x:Int) => !p(x))

  /**
   * Returns a set transformed by applying `f` to each element of `s`.
   * 
   * So what does that mean, exactly?
   * Consider s1 = singletonSet(1), which evals to (x:Int) => x == 1
   * consider f(x:Int) = 2*x
   * The result of map(s1, f) should produce a Set characteristic function 
   * that returns true for argument 2 (and 2 alone).
   * 
   * 2 is the result of f(x).  We don't have x (and we can't "undo" f from 2).
   * So we must iterate all possible values in the set, and if there exists a value
   * that, when applied to f, == 2, then the answer is true.
   * 
   */
  def map(s: Set, f: Int => Int): Set = (x:Int) => exists(s, (y:Int) => f(y) == x)

  /**
   * Displays the contents of a set
   */
  def toString(s: Set): String = {
    val xs = for (i <- -bound to bound if contains(s, i)) yield i
    xs.mkString("{", ",", "}")
  }

  /**
   * Prints the contents of a set on the console.
   */
  def printSet(s: Set) {
    println(toString(s))
  }
}
