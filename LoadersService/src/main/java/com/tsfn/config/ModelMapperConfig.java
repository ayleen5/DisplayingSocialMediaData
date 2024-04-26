package com.tsfn.config;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tsfn.model.Facebook;
import com.tsfn.model.Instagram;
import com.tsfn.model.LinkedIn;
import com.tsfn.model.Loader;
@Configuration
public class ModelMapperConfig {

    @Bean
    ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        
//         Mapping from Instagram to LoaderDTO
        modelMapper.addMappings(new PropertyMap<Instagram, Loader>() {
            @Override
            protected void configure() {
                skip(destination.getId()); // Exclude mapping for id
                map().setPostId(source.getPostId());
                map().setContentType(source.getPostType());
                map().setImpressions(source.getImpressions());
                map().setViews(source.getReach());
                map().setClicks(source.getSaves());
                map().setLikes(source.getLikes()); 
                map().setComments(source.getComments());
                map().setShares(source.getShares());
                LocalDateTime t = LocalDateTime.now();
                map().setTimestamp(t);
                
            }
        });

        // Post-mapping operations
        modelMapper.getTypeMap(Instagram.class, Loader.class).setPostConverter(context -> {
            Instagram source = context.getSource();
            Loader destination = context.getDestination();
            if (source.getImpressions() > 0 && source.getSaves() > 0) {
                destination.setCTR(source.getSaves() / (double) source.getImpressions());
            } else {
                destination.setCTR(0);
            }
            if (source.getReach() > 0) {
                destination.setEngagementrate(source.getLikes() / (double) source.getReach());
            } else {
                destination.setEngagementrate(0);
            }
            return destination;
        });
        
        

     // Mapping from Facebook to LoaderDTO
        modelMapper.addMappings(new PropertyMap<Facebook, Loader>() {
            @Override
            protected void configure() {
                skip(destination.getId()); // Exclude mapping for id
                map().setPostId(source.getPostId());
                map().setContentType(source.getPostType());
                map().setImpressions(source.getImpressions());
                
                map().setViews(source.getReach());
                map().setClicks(source.getTotalClicks());
                
                map().setLikes(source.getReactions()); 
                map().setComments(source.getComments());
                map().setShares(source.getShares());
                
                
                
            }
        });
        
     // Post-mapping operations
        modelMapper.getTypeMap(Facebook.class, Loader.class).setPostConverter(context -> {
            Facebook source = context.getSource();
            Loader destination = context.getDestination();
            if( source.getImpressions() == 0)
            	destination.setCTR(0);
            else
            	destination.setCTR(source.getTotalClicks()/ source.getImpressions());
            if( source.getReach() == 0)
            	destination.setEngagementrate(0);
            else
            	destination.setEngagementrate(source.getReactionsCommentsShares()/ source.getReach());
           
            return destination;
        });
     
              // Mapping from LinkedIn to LoaderDTO
        modelMapper.addMappings(new PropertyMap<LinkedIn, Loader>() {
            @Override
            protected void configure() {
                skip(destination.getId()); // Exclude mapping for id
                map().setPostId(source.getPostLink());
                map().setContentType(source.getContentType());
                map().setImpressions(source.getImpressions());
                map().setViews(0.0);
                map().setClicks(source.getClicks());
                map().setCTR(source.getCTR());
                map().setLikes(source.getLikes()); 
                map().setComments(source.getComments());
                map().setShares(source.getReposts());
                map().setEngagementrate(source.getEngagementRate());
               // map().setTimestamp(source.getPublishTime());
                
            }
        });
    


        return modelMapper;
    
            }
}
