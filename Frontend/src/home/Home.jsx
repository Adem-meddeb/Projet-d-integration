import React from 'react';
import Banner from '../components/banner';
import EventList from '../components/EventList';
import EventHighlight from './FavHighlight';



const Home = () => {
  return (
    <div>
   <Banner />
   
   <EventHighlight />
   
    </div>
  );
};

export default Home;
