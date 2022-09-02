package fr.twizox.kinkobot.databases;

import com.j256.ormlite.support.ConnectionSource;
import org.slf4j.Logger;

public abstract class Database {

    protected final String name;
    protected final String path;
    protected final Logger logger;
    protected ConnectionSource connectionSource;

    protected Database(String name, String path) {
        this(name, path, null);
    }

    protected Database(String name, String path, Logger logger) {
        this.name = name;
        this.path = path;
        this.logger = logger;
    }

    public String getName() {
        return name;
    }

    public abstract boolean init();

    public abstract void close();

}
