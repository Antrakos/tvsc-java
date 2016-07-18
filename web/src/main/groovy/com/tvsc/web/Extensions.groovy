package com.tvsc.web

import com.google.common.collect.ImmutableBiMap
import com.tvsc.core.model.Episode
import com.tvsc.core.model.Season
import com.tvsc.core.model.Serial
import com.tvsc.web.exception.MappingException
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

/**
 *
 * @author Taras Zubrei
 */
@Component
class Extensions {
    @Autowired
    private ModelMapper modelMapper
    static ImmutableBiMap mappings = ImmutableBiMap.builder()
            .put(Episode, EpisodeDto)
            .put(Serial, SerialDto)
            .put(Season, SeasonDto)
            .build()

    @PostConstruct
    def init() {
        mappings.collect { key, value -> [key, value] }.flatten().forEach {
            it.metaClass.convert = {
                new MappingException('Cannot convert unknown type').wrap {
                    modelMapper.map(delegate.delegate, Extensions.mappings.get(delegate.delegate.class)
                            ?: Extensions.mappings.inverse().get(delegate.delegate.class))
                }
            }
        }
    }
}