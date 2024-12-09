import React from "react";
import EventImage from "../assets/EventImage.jpg"; // Image représentative pour vos événements
import { Link } from "react-router-dom";

const EventHighlight = () => {
  return (
    <div className="px-4 lg:px-24 my-20 flex flex-col md:flex-row justify-between items-center gap-12">
      <div className="md:w-1/2">
        <img src={EventImage} className="rounded md:w-10/12" alt="Event"></img>
      </div>
      {/*leading-snug :	line-height: 1.375; */}
      <div className="md:w-1/2 space-y-6 content-center">
        <h2 className="text-5xl font-bold leading-snug">
          Explore Amazing{" "}
          <span className="text-blue-700">Events Around You!</span>
        </h2>
        <p className="mb:10 text-lg md:w-5/6">
          Découvrez une variété d'événements, allant des conférences aux
          concerts. Rejoignez-nous pour créer, partager et vivre des expériences
          inoubliables.
        </p>
        <div>
          <h3 className="text-3xl font-bold">10+</h3>
          <p className="text-base">Organisateurs inscrits</p>
        </div>
        <div>
          <h3 className="text-3xl font-bold">20+</h3>
          <p className="text-base">Événements créés</p>
        </div>
        <div>
          <h3 className="text-3xl font-bold">50+</h3>
          <p className="text-base">Participants enregistrés</p>
        </div>
        <Link to="/events">
          <button className="bg-blue-600 text-white rounded px-5 py-2 font-semibold hover:bg-black transition-all duration-300 mt-12 block">
            Voir les événements
          </button>
        </Link>
      </div>
    </div>
  );
};

export default EventHighlight;
