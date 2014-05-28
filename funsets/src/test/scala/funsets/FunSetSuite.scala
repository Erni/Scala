package funsets

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {

  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  test("string take") {
    val message = "hello, world"
    assert(message.take(5) == "hello")
  }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  test("adding ints") {
    assert(1 + 2 === 3)
  }

  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) contains 1") {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3".
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
      assert(!contains(s1, 2), "Singleton 2")
      assert(contains(s2, 2), "Singleton 3")
    }
  }

  test("union contains all elements") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("intersection contains all elements") {
    new TestSets {
      val su = union(s1, s2) // 1, 2
      val si1 = intersect(su, s1) // 1
      val si2 = intersect(su, s2) // 2
      val si3 = intersect(su, si1) // 1
      assert(contains(su, 1), "intersection 1")
      assert(contains(su, 2), "intersection 2")
      assert(!contains(su, 3), "intersection 3")
      assert(contains(si1, 1), "intersection 4")
      assert(contains(si2, 2), "intersection 5")
      assert(contains(si3, 1), "intersection 6")
      assert(!contains(si1, 2), "intersection 7")
      assert(!contains(si2, 1), "intersection 8")
      assert(!contains(si3, 2), "intersection 9")
    }
  }

  test("diff contains all elements") {
    new TestSets {
      val s = union(union(s1, s2), s3) // 1, 2, 3
      val ss = union(s1, s2) // 1, 2

      val dss = diff(s, ss) // 3

      assert(!contains(dss, 1), "diff 1")
      assert(!contains(dss, 2), "diff 2")
      assert(contains(dss, 3), "diff 3")
    }
  }

  test("filter contains all elements") {
    new TestSets {
      val s = union(union(s1, s2), s3) // 1, 2, 3
      val fs = filter(s, (x: Int) => x % 2 == 0)

      assert(!contains(fs, 1), "filter 1")
      assert(contains(fs, 2), "filter 2")
      assert(!contains(fs, 3), "filter 3")
    }
  }

  test("forall test") {
    new TestSets {
      val s = union(union(s1, s2), s3) // 1, 2, 3

      assert(forall(s, x => x < 4), "forall 1")
      assert(!forall(s, x => x < 3), "forall 2")
      assert(forall(s, x => x > 0), "forall 3")
      assert(forall(s, x => x == 1 || x == 2 || x == 3), "forall 4")
    }
  }

  test("exists test") {
    new TestSets {
      val s = union(union(s1, s2), s3) // 1, 2, 3

      assert(exists(s1, x => x < 3), "exists 1")
      assert(exists(s, x => x < 4), "exists 2")
      assert(exists(s, x => x < 3), "exists 3")
      assert(exists(s, x => x < 2), "exists 4")
      assert(!exists(s, x => x < 1), "exists 5")
      assert(exists(s, x => x < 10), "exists 6")
      assert(!exists(s, x => x == 10), "exists 7")
      assert(exists(s, x => x == 1), "exists 8")
      assert(exists(s, x => x == 2), "exists 9")
      assert(exists(s, x => x == 3), "exists 10")
      assert(!exists(s, x => x == 4), "exists 11")
    }
  }

  test("map test") {
    new TestSets {
      val s = union(union(s1, s2), s3) // 1, 2, 3
      val sMapped = map(s, (x: Int) => x + 3) // 4, 5, 6
      assert(!contains(sMapped, 1), "map 1")
      assert(!contains(sMapped, 2), "map 2")
      assert(!contains(sMapped, 3), "map 3")
      assert(contains(sMapped, 4), "map 4")
      assert(contains(sMapped, 5), "map 5")
      assert(contains(sMapped, 6), "map 6")
      assert(!contains(sMapped, 7), "map 7")
    }
  }

}
