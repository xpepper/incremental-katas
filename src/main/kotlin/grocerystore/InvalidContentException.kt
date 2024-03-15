package grocerystore

import java.lang.RuntimeException

class InvalidContentException(rawEntry: String) : RuntimeException("Invalid entry '$rawEntry' found in the ROS file.")
