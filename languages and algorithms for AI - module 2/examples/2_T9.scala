import scala.io.Source
object T9 {
  def main(args: Array[String]) {
    // Download a list of possible words
    val in = Source.fromURL("http://cs.unibo.it/zavattar/words.txt")
    val word = in.getLines.toList filter (w => w forall (c => c.isLetter))

    // Map which associates T9 digits to Strings containing the characters it can generate
    val mnem = Map(
                   '2'->"ABC", '3'->"DEF",
      '4'->"GHI",  '5'->"JKL", '6'->"MNO",
      '7'->"PQRS", '8'->"TUV", '9'->"WXYZ"
    )

    // Extracts from mnem a map associating characters to corrisponding single T9 digits
    val charCode: Map[Char, Char] =
      for {
        (digit, str) <- mnem
        ltr <- str
      } yield ltr -> digit

    // Calculates the single T9 digit sequence necessary for generating a string
    def wordCode(word: String): String =
      word.toUpperCase map charCode

    // Creates a list of Strings which can be generated from a series of single T9 digits
    val wordsForNum: Map[String, Seq[String]] =
      word groupBy wordCode withDefaultValue Seq()

    // Gets all the combinations of lists of Strings which can be generated from a series of single T9 digits
    def encode(number: String): Set[List[String]] =
      if (number.isEmpty) Set(List())
      else (
        for {
          // All the possible first split positions in the digit String
          split <- 1 to number.length
          // The first n digits from the digit String
          word <- wordsForNum(number take split)
          // Recursively generate the decoding of the remainder of the digit String
          rest <- encode(number drop split)
        } yield word :: rest
        // Create a list containing the result of the two splits of the digit String
        // Similar but different from concatenation
      ).toSet

    // 7225247386 => "scala" "is" "fun"
    val encodedStrings = encode("7225247386")
    //println(encodedStrings)
    encodedStrings.foreach((x) => {
      println()
      x.foreach(println)
    })
  }
}
