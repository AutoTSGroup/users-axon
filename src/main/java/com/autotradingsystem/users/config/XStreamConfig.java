package com.autotradingsystem.users.config;

import com.autotradingsystem.messages.commands.broker.*;
import com.autotradingsystem.messages.commands.broker.share.AddShareCommand;
import com.autotradingsystem.messages.commands.broker.share.RemoveShareCommand;
import com.autotradingsystem.messages.commands.broker.share.UpdateShareCommand;
import com.autotradingsystem.messages.commands.trading.StartTradingCommand;
import com.autotradingsystem.messages.commands.trading.StopTradingCommand;
import com.autotradingsystem.messages.commands.user.AttachBrokerToUserCommand;
import com.autotradingsystem.messages.commands.user.CreateUserCommand;
import com.autotradingsystem.messages.commands.user.DisAttachBrokerToUserCommand;
import com.autotradingsystem.messages.dto.ShareDto;
import com.autotradingsystem.messages.events.broker.*;
import com.autotradingsystem.messages.events.broker.share.ShareAddedEvent;
import com.autotradingsystem.messages.events.broker.share.ShareRemovedEvent;
import com.autotradingsystem.messages.events.broker.share.ShareUpdatedEvent;
import com.autotradingsystem.messages.events.trading.TradingStartedEvent;
import com.autotradingsystem.messages.events.trading.TradingStoppedEvent;
import com.autotradingsystem.messages.events.user.AttachedBrokerToUserEvent;
import com.autotradingsystem.messages.events.user.DisAttachedBrokerToUserEvent;
import com.autotradingsystem.messages.events.user.UserCreatedEvent;
import com.autotradingsystem.messages.queries.borker.*;
import com.thoughtworks.xstream.XStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XStreamConfig {

    @Bean
    public XStream xStream() {
        XStream xStream = new XStream();

        registerClasses(xStream, CreateUserCommand.class, UserCreatedEvent.class,
                AttachBrokerToUserCommand.class, AttachedBrokerToUserEvent.class,
                DisAttachBrokerToUserCommand.class, DisAttachedBrokerToUserEvent.class);
        registerClasses(xStream, CreateBrokerCommand.class, CreatedBrokerEvent.class,
                DeleteBrokerCommand.class, DeletedBrokerEvent.class,
                UpdateBrokerCommand.class, UpdatedBrokerEvent.class, AddShareCommand.class,
                ShareAddedEvent.class, RemoveShareCommand.class, ShareRemovedEvent.class,
                ShareUpdatedEvent.class, UpdateShareCommand.class);
        registerClasses(xStream, FindBrokerIdByUserIdQuery.class, FindUserIdByBrokerIdQuery.class,
                FindShareIdByBrokerIdQuery.class, FindShareDtoByBrokerIdAndFigiQuery.class, ShareDto.class,
                FindShareDtoListByUserIdAndBrokerNameQuery.class,
                FindShareDtoByShareFigiAndUserIdQuery.class,FindShareDtoListByUserIdAndBrokerIdQuery.class);
        registerClasses(xStream, StartTradingCommand.class, StopTradingCommand.class, TradingStartedEvent.class,
                TradingStoppedEvent.class, PlaceBuyOrderCommand.class, PlaceSellOrderCommand.class,
                PlacedSellOrderEvent.class, PlacedBuyOrderEvent.class);
        return xStream;
    }

    private void registerClasses(XStream xStream, Class<?>... classes) {
        for (Class<?> clazz : classes) {
            xStream.allowTypeHierarchy(clazz);
        }
    }

}
