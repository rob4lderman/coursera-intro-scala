package forcomp

import common._

object Anagrams {

  /** A word is simply a `String`. */
  type Word = String

  /** A sentence is a `List` of words. */
  type Sentence = List[Word]

  /** `Occurrences` is a `List` of pairs of characters and positive integers saying
   *  how often the character appears.
   *  This list is sorted alphabetically w.r.t. to the character in each pair.
   *  All characters in the occurrence list are lowercase.
   *  
   *  Any list of pairs of lowercase characters and their frequency which is not sorted
   *  is **not** an occurrence list.
   *  
   *  Note: If the frequency of some character is zero, then that character should not be
   *  in the list.
   */
  type Occurrences = List[(Char, Int)]

  /** The dictionary is simply a sequence of words.
   *  It is predefined and obtained as a sequence using the utility method `loadDictionary`.
   */
  val dictionary: List[Word] = loadDictionary

  /** Converts the word into its character occurence list.
   *  
   *  Note: the uppercase and lowercase version of the character are treated as the
   *  same character, and are represented as a lowercase character in the occurrence list.
   */
  def wordOccurrences(w: Word): Occurrences = 
    w.toLowerCase.groupBy( c => c).mapValues( w => w.length ).toList.sortWith( _._1 < _._1)

  /** Converts a sentence into its character occurrence list. */
  def sentenceOccurrences(s: Sentence): Occurrences = 
    wordOccurrences( s.fold("")(_ + _) )

  /** The `dictionaryByOccurrences` is a `Map` from different occurrences to a sequence of all
   *  the words that have that occurrence count.
   *  This map serves as an easy way to obtain all the anagrams of a word given its occurrence list.
   *  
   *  For example, the word "eat" has the following character occurrence list:
   *
   *     `List(('a', 1), ('e', 1), ('t', 1))`
   *
   *  Incidentally, so do the words "ate" and "tea".
   *
   *  This means that the `dictionaryByOccurrences` map will contain an entry:
   *
   *    List(('a', 1), ('e', 1), ('t', 1)) -> Seq("ate", "eat", "tea")
   *
   */
  lazy val dictionaryByOccurrences: Map[Occurrences, List[Word]] = 
    loadDictionary.groupBy( w => wordOccurrences(w) ).withDefaultValue(List())

  /** Returns all the anagrams of a given word. */
  def wordAnagrams(word: Word): List[Word] = 
    dictionaryByOccurrences( wordOccurrences(word) )

    
       
  /**
   * Computes all possible permutations of the given occurrence.
   * The 0th occurrence is included as well (the "empty" permutation).
   * 
   * (a,2) => List((a,0),(a,1),(a,2))
   * 
   */
  def permutations( p : (Char,Int) ) : List[(Char,Int)] = (for (x <- 0 to p._2) yield (p._1,x)).toList
//    p match {
//    	case (c,1) => List((c,1))
//    	case (c,n) => (c,n) :: permutations((c,n-1))
//    }
  
  /**
   * combine all elements from list1 with each list in list2.
   * or.. ll2 flatMap ( l => l1 map (x => x :: l) )
   * 
   */
  def combine[T]( l1: List[T], ll2 : List[List[T]] ) : List[List[T]] = 
    for (x <- l1; y <- ll2) yield x :: y
 
    
  /** Returns the list of all subsets of the occurrence list.
   *  This includes the occurrence itself, i.e. `List(('k', 1), ('o', 1))`
   *  is a subset of `List(('k', 1), ('o', 1))`.
   *  It also include the empty subset `List()`.
   * 
   *  Example: the subsets of the occurrence list `List(('a', 2), ('b', 2))` are:
   *
   *    List(
   *      List(),
   *      List(('a', 1)),
   *      List(('a', 2)),
   *      List(('b', 1)),
   *      List(('a', 1), ('b', 1)),
   *      List(('a', 2), ('b', 1)),
   *      List(('b', 2)),
   *      List(('a', 1), ('b', 2)),
   *      List(('a', 2), ('b', 2))
   *    )
   *
   *  Note that the order of the occurrence list subsets does not matter -- the subsets
   *  in the example above could have been displayed in some other order.
   *  
   *  for each pair, compute combinations:
   *  (a,2) => (a,0), (a,1), (a,2)
   *  
   *  combine with every other pair's combinations
   * 
   *  
   *  Basically we want to generate all combinations of a list of sets.
   *  List( Set(x1,x2,x3), Set(y1,y2,y3), Set(z1,z2,z3) )'
   *  
   *  Normally we'd use a for loop, iterating over each set, but in this
   *  case, we don't know how many sets we have.  So instead, we solve
   *  recursively by first combining two sets, producing a 
   *  new set of all combinations of set1 and set2, then combine this with
   *  set3, and so on, until we've iterated over all.
   *  
   *  This sounds like a fold.
   *  
   *  So the base case is an empty List[List]. This is combined with
   *  first Set of permutations, returning a new List[List], which is then
   *  combined with the next Set of permutations, and so on.
   *  
   */
 
    
  def combinations(occurrences: Occurrences ): List[ Occurrences ] = { 
    val memo: List[Occurrences] = List(List()) // empty base case for fold
    
    // Map the list of occurrences to a List of Lists where each new List
    // is all the possible permutations of each corresponding occurrence.
    // E.g. List( (a,2) ) => List( List( (a,1), (a,2) ) )
    val occurrencesWithPermutations: List[ Occurrences ] = occurrences map (p => permutations(p))

    // Compute all combinations by folding up all the permutations.
    // This works by first combining a Set of permutations with the empty list (base case)
    // to produce a new List[List] with each permutation in its own list.
    // That list is then combined with the next set of permutations to produce a 
    // new List[List] with each permutation concatenated with each list.
    val allCombos : List[Occurrences] = occurrencesWithPermutations.foldRight(memo)(combine) 
    
    // allCombos includes 0 occurrences.  Filter those out.
    allCombos map ( l => l filter (p => p._2 > 0) )
  }
    
  /** Subtracts occurrence list `y` from occurrence list `x`.
   * 
   *  The precondition is that the occurrence list `y` is a subset of
   *  the occurrence list `x` -- any character appearing in `y` must
   *  appear in `x`, and its frequency in `y` must be smaller or equal
   *  than its frequency in `x`.
   *
   *  Note: the resulting value is an occurrence - meaning it is sorted
   *  and has no zero-entries.
   *  
   *  List((a,2),(b,1)) - List((a,1)) = List((a,1),(b,1))
   *  
   *  if (x._1 != y._1) x :: subtract(xs, y)
   *  else (x._1, x._2 - y._2) :: subtract(xs, ys)
   *  
   */
  def subtract(x: Occurrences, y: Occurrences): Occurrences = {
    if ( y.isEmpty ) x
    else {
    	// Check the head of each list.  If they match (same char)
        // then create a new pair by subtracting the frequencies, 
        // and concatenate with the recursive subtraction of the 
        // remainder xs and the remainder ys.  If they don't match
        // then return xh concatenated with the remainder xs and y
        // (not ys -- since we didn't match the head of y, we don't
        // remove it from the list).
    	val (xh :: xs) = x
    	val (yh :: ys) = y
    	if (xh._1 == yh._1) (xh._1, xh._2-yh._2) :: subtract(xs, ys)
    	else xh :: subtract(xs, y)
    } filter ( p => p._2 > 0 )  // Finally, filter out any freqs that dropped to 0.
  }

  /** Returns a list of all anagram sentences of the given sentence.
   *  
   *  An anagram of a sentence is formed by taking the occurrences of all the characters of
   *  all the words in the sentence, and producing all possible combinations of words with those characters,
   *  such that the words have to be from the dictionary.
   *
   *  The number of words in the sentence and its anagrams does not have to correspond.
   *  For example, the sentence `List("I", "love", "you")` is an anagram of the sentence `List("You", "olive")`.
   *
   *  Also, two sentences with the same words but in a different order are considered two different anagrams.
   *  For example, sentences `List("You", "olive")` and `List("olive", "you")` are different anagrams of
   *  `List("I", "love", "you")`.
   *  
   *  Here is a full example of a sentence `List("Yes", "man")` and its anagrams for our dictionary:
   *
   *    List(
   *      List(en, as, my),
   *      List(en, my, as),
   *      List(man, yes),
   *      List(men, say),
   *      List(as, en, my),
   *      List(as, my, en),
   *      List(sane, my),
   *      List(Sean, my),
   *      List(my, en, as),
   *      List(my, as, en),
   *      List(my, sane),
   *      List(my, Sean),
   *      List(say, men),
   *      List(yes, man)
   *    )
   *
   *  The different sentences do not have to be output in the order shown above - any order is fine as long as
   *  all the anagrams are there. Every returned word has to exist in the dictionary.
   *  
   *  Note: in case that the words of the sentence are in the dictionary, then the sentence is the anagram of itself,
   *  so it has to be returned in this list.
   *
   *  Note: There is only one anagram of an empty sentence.
   *  
   */
  def sentenceAnagrams(sentence: Sentence): List[Sentence] = {
    
    def helper(wordsSoFar: List[Word], remainder: Occurrences) : List[ List[Word] ] = {
      if (remainder.isEmpty) List(wordsSoFar)
      else (for {
    	  subset <- combinations(remainder)
    	  word <- dictionaryByOccurrences(subset)
      } yield helper( word :: wordsSoFar, subtract(remainder, subset) ) ). flatten
    }
    
    helper(List(), sentenceOccurrences(sentence))
  }
  
   
}
