package org.fordes.adfs.config;

import lombok.Data;
import org.apache.logging.log4j.util.Strings;
import org.fordes.adfs.enums.HandleType;
import org.fordes.adfs.enums.RuleSet;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Component
@ConfigurationProperties(prefix = "application.rule")
public class InputProperties {

    private Set<Prop> remote = Set.of();
    private Set<Prop> local = Set.of();

    public Stream<Map.Entry<HandleType, Prop>> stream() {
        return Stream.concat(local.stream().map(e -> Map.entry(HandleType.LOCAL, e)),
                remote.stream().map(e -> Map.entry(HandleType.REMOTE, e)));
    }

    public boolean isEmpty() {
        return (remote == null || remote.isEmpty()) && (local == null || local.isEmpty());
    }

    public void setRemote(Set<Prop> remote) {
        this.remote = Optional.ofNullable(remote)
                .map(e -> e.stream().filter(p -> !p.path.isEmpty()).collect(Collectors.toSet())).orElse(Set.of());
    }

    public void setLocal(Set<Prop> local) {
        this.local = Optional.ofNullable(local)
                .map(e -> e.stream().filter(p -> !p.path.isEmpty()).collect(Collectors.toSet())).orElse(Set.of());
    }

    public record Prop(String name, RuleSet type, String path) {

        public Prop(String name, RuleSet type, String path) {
            this.type = Optional.ofNullable(type).orElse(RuleSet.EASYLIST);
            this.name = Optional.ofNullable(name).orElse(Strings.EMPTY).trim();
            this.path = Optional.ofNullable(path).orElse(Strings.EMPTY).trim();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Prop prop) {
                return prop.path.equals(this.path);
            }
            return false;
        }

        public int hashCode() {
            return path.hashCode();
        }
    }
}
