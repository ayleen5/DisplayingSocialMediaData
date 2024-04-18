package com.tsfn.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tsfn.dto.LoaderDTO;
import com.tsfn.model.Instagram;
import com.tsfn.model.Facebook;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        
        modelMapper.addMappings(new PropertyMap<Instagram, LoaderDTO>() {
            @Override
            protected void configure() {

                map().setTimestamp(source.getTimestamp());
                map().setPostID(source.getPostId());
                map().setImpressions(source.getImpressions());
                map().setCTR(source.getCTR());
                map().setContentType(source.getPosttype()); 
                map().setViews(source.getReach());
                map().setClicks(source.getSaves()); 
                map().setLikes(source.getLikes());
                map().setComments(source.getComments());
                map().setShares(source.getShares());
                map().setEngagementrate(source.getEngagementrate());

            }
        });
        modelMapper.addMappings(new PropertyMap<LoaderDTO, Instagram>() {
            @Override
            protected void configure() {
                map().setTimestamp(source.getTimestamp());
                map().setPostId(source.getPostID());
                map().setImpressions(source.getImpressions());
                map().setCTR(source.getCTR());
                map().setPosttype(source.getContentType()); 
                map().setReach(source.getViews());
                map().setSaves(source.getClicks());     
                map().setLikes(source.getLikes());
                map().setComments(source.getComments());
                map().setShares(source.getShares());
                map().setEngagementrate(source.getEngagementrate());
            }
        });


        modelMapper.addMappings(new PropertyMap<Facebook, LoaderDTO>() {
           @Override
           protected void configure() {
        	   map().setTimestamp(source.getTimestamp());
               map().setPostID(source.getPostId());
               map().setImpressions(source.getImpressions());
               map().setCTR(source.getCTR());
               map().setContentType(source.getPosttype()); 
               map().setViews(source.getReach());
               map().setClicks(source.getTotalclicks()); // שנה את הפונקציה לקריאה לשדה המתאים במודל, כלומר getSaves()
               map().setLikes(source.getReactions());
               map().setComments(source.getComments());
               map().setShares(source.getShares());
               map().setEngagementrate(source.getEngagementrate());

           }
       });
        modelMapper.addMappings(new PropertyMap<LoaderDTO, Facebook>() {
            @Override
            protected void configure() {
                map().setTimestamp(source.getTimestamp());
                map().setPostId(source.getPostID());
                map().setImpressions(source.getImpressions());
                map().setCTR(source.getCTR());
                map().setPosttype(source.getContentType()); 
                map().setReach(source.getViews());
                map().setTotalclicks(source.getClicks());     
                map().setReactions(source.getLikes());
                map().setComments(source.getComments());
                map().setShares(source.getShares());
                map().setEngagementrate(source.getEngagementrate());
            }
        });

        return modelMapper;
    }
}

