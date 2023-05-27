package meteordevelopment.meteorclient.vfs;

import org.reflections.vfs.Vfs;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class QuiltDir implements Vfs.Dir {
    Object root;
	Object fs;
	public QuiltDir(Object root) {
		this.root = root;
		try {
			this.fs = Object.class.getDeclaredMethod("getFileSystem").invoke(root);
		} catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getPath() {
		return root.toString().replace("\\", "/");
	}

	@Override
	public Iterable<Vfs.File> getFiles() {
		try {
			return Files.walk((Path) root).filter(Files::isRegularFile).map(a -> new QuiltFile((Path) a, fs)).collect(Collectors.toList());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
