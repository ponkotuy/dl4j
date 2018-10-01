package utils

import com.github.fracpete.processoutput4j.core.{StreamingProcessOutputType, StreamingProcessOwner}
import com.github.fracpete.processoutput4j.output.StreamingProcessOutput
import com.github.fracpete.rsync4j.RSync

class RsyncWrapper(stream: StreamingProcessOwner = Streams.Stdout)(defaults: RsyncOption*) {
  def run(source: String, dest: String, options: RsyncOption*): Unit = {
    val opts = (options ++ defaults).distinct
    val rsync = new RSync().source(source).destination(dest)
    val set = opts.foldLeft[RSync](rsync) { (rs, opt) => opt.set(rs) }
    val out = new StreamingProcessOutput(stream)
    out.monitor(set.builder())
  }
}

sealed abstract class RsyncOption {
  def set(rsync: RSync): RSync
}

object RsyncOption {
  case object Archive extends RsyncOption {
    override def set(rsync: RSync): RSync = rsync.archive(true)
  }

  case object Recursive extends RsyncOption {
    override def set(rsync: RSync): RSync = rsync.recursive(true)
  }

  case object Delete extends RsyncOption {
    override def set(rsync: RSync): RSync = rsync.delete(true)
  }

  case object Verbose extends RsyncOption {
    override def set(rsync: RSync): RSync = rsync.verbose(true)
  }

  case object CopyLinks extends RsyncOption {
    override def set(rsync: RSync): RSync = rsync.copyLinks(true)
  }

  case class Rsh(command: String) extends RsyncOption {
    override def set(rsync: RSync): RSync = rsync.rsh(command)
  }
}

object Streams {
  val Stdout = new StreamingProcessOwner {
    override val getOutputType: StreamingProcessOutputType = StreamingProcessOutputType.BOTH

    override def processOutput(line: String, stdout: Boolean): Unit =
      println(s"${if(stdout) "[STDOUT]" else "[STDERR]"} ${line}")
  }
}
