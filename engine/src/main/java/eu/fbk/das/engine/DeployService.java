package eu.fbk.das.engine;

import eu.fbk.das.domainobject.core.entity.DomainObjectDefinition;
import eu.fbk.das.domainobject.core.entity.jaxb.DomainObject;
import eu.fbk.das.domainobject.core.entity.jaxb.DomainProperty;
import eu.fbk.das.domainobject.core.entity.jaxb.Fragment;
import eu.fbk.das.domainobject.core.entity.jaxb.Process;
import eu.fbk.das.domainobject.core.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceArrayPropertyEditor;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DeployService {

    private static final Logger LOG = LoggerFactory.getLogger(DeployService.class);

    public DomainObjectDefinition deploy() {
        DomainObject domainObject = getDeploymentDomainObject();
        DomainObjectDefinition dod = deployDomainObject(domainObject);
        LOG.debug("Domain Object Definition for deployment {}", dod);
        return dod;
    }

    public DomainObject getDeploymentDomainObject() {
        ResourceArrayPropertyEditor resolver = new ResourceArrayPropertyEditor();
        String[] domainObjectPattern = new String[]{"classpath*:do/*.xml"};
        resolver.setValue(domainObjectPattern);
        Resource[] domainObject = (Resource[]) resolver.getValue();
        try {
            File f = domainObject[0].getFile();
            return Parser.parseDomainObject(f);
        } catch (IOException e) {
            LOG.error("Domain Object file missed");
            e.printStackTrace();
        }
        return null;
    }

    public DomainObjectDefinition deployDomainObject(DomainObject e) {
        Process process = null;
        List<Fragment> fragments = null;
        List<DomainProperty> properties = null;
        try {
            process = parseProcess(e);
            fragments = parseFragments(e);
            properties = parseProperties(e);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return new DomainObjectDefinition(e, process, fragments, properties);
    }

    public Process parseProcess(DomainObject e) throws Exception {
        if (e.getProcess() == null) {
            LOG.error("ProcessDefEntity must be not null");
        }
        File f = getFile(e.getProcess().getName());
        if (f != null) {
            return Parser.parseProcess(f);
        }
        return null;
    }

    public List<Fragment> parseFragments(DomainObject e) throws Exception {
        if (e.getFragment() == null) {
            return null;
        }
        return e.getFragment().stream().map(frag -> {
            try {
                File f = getFile(frag.getName());
                if (f != null) {
                    return Parser.parseFragment(f);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
    }

    public List<DomainProperty> parseProperties(DomainObject e) {
        if (e.getDomainKnowledge() == null) {
            return null;
        }
        if (e.getDomainKnowledge().getInternalDomainProperty() != null) {
            return e.getDomainKnowledge().getInternalDomainProperty().stream().map(dp -> {
                try {
                    File f = getFile(dp.getName());
                    if (f != null) {
                        return Parser.parseDomainProperty(f);
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());
        }
        //TODO: what to do with External Domain Property
        return null;
    }


    public File getFile(String path) throws Exception {
        ResourceArrayPropertyEditor resolver = new ResourceArrayPropertyEditor();
        String[] pattern = new String[]{String.format("classpath*:do/%s.xml", path)};
        LOG.debug("{}", pattern);
        resolver.setValue(pattern);
        Resource[] resources = (Resource[]) resolver.getValue();
        try {
            return resources[0].getFile();
        } catch (IOException e) {
            LOG.error("File missed");
            e.printStackTrace();
        }
        return null;
    }

}
