package org.geoserver.rest.edit;

import org.geoserver.rest.edit.mapper.PoiMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author ily
 * @date 04 22, 2020
 * @since 1.0.0
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
public class SampleTest {
    @Autowired private PoiMapper poiMapper;

    @Test
    public void testSelect() {}
}
