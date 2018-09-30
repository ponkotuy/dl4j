import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes

import labels.Classification
import utils.Files

object Registration {

  def main(args: Array[String]): Unit = {
    import utils.Path._
    import Classification._

    Files.find(originalImagePath){ (path: Path, attr: BasicFileAttributes) =>
      attr.isDirectory && Classification.names.contains(path.getFileName.toString)
    }.foreach { dir =>
      println(s"Target directory: ${dir}")
      Classification.findName(dir.getFileName.toString).get match {
        case NonH =>
          Files.find(dir)(isImage).foreach { file =>
            println(s"  NonH target file: ${file.getFileName}")
            val parent = file.getParent.getParent.getFileName
            val dest = NonH.path.resolve(parent)
            Files.mkdirs(dest)
            Files.move(file, dest.resolve(file.getFileName))
          }
        case Ero =>
          Files.find(dir)(isImage).foreach { file =>
            println(s"  Ero target file: ${file.getFileName}")
            val parent = file.getParent.getParent
            val moved = Files.move(file, parent.resolve(file.getFileName))
            val linkDir = Ero.path.resolve(parent.getFileName)
            Files.mkdirs(linkDir)
            Files.createSymbolicLink(linkDir.resolve(file.getFileName), moved)
          }
      }
      Files.delete(dir)
    }
  }

  def isImage(path: Path, attr: BasicFileAttributes): Boolean = {
    !attr.isDirectory && {
      val Array(typ, _) = Files.probeContentType(path).split('/')
      typ.contains("image")
    }
  }
}
