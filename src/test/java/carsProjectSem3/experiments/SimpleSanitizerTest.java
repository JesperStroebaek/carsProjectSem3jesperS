package carsProjectSem3.experiments;

class SimpleSanitizerTest {


  //@Test
  void simpleSanitizeTest() {
    String result = SimpleSanitizer.simpleSanitize("Hello <b>World</b>");
    //assertEquals("Hello World",result);
  }

}